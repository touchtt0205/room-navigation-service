package com.app.room_navigation_service.service;

import com.app.room_navigation_service.DTO.StepUsageDTO;
import com.app.room_navigation_service.entity.RouteStep;
import com.app.room_navigation_service.entity.Step;
import com.app.room_navigation_service.repository.RouteStepRepository;
import com.app.room_navigation_service.repository.StepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class StepService {

    @Autowired
    private final StepRepository stepRepository;
    @Autowired
    private final StorageService storageService;
    @Autowired
    private RouteStepRepository routeStepRepository;

    public StepService(StepRepository stepRepository, StorageService storageService) {
        this.stepRepository = stepRepository;
        this.storageService = storageService;
    }

    // Create / Update
    public Step saveStep(Step step) {
        return stepRepository.save(step);
    }

    // Read All
    public List<Step> getAllSteps() {
        return stepRepository.findAll();
    }

    // Read One
    public Optional<Step> getStepById(Integer id) {
        return stepRepository.findById(id);
    }

    // Delete
    public void deleteStep(Integer id) {
        stepRepository.deleteById(id);
    }

    public StepUsageDTO getUsage(Long stepId) {
        long count = routeStepRepository.countRoutesUsingStep(stepId);
        List<String> roomNames = routeStepRepository.findRoomNamesByStepId(stepId);

        return new StepUsageDTO(
                count > 1,
                count,
                roomNames
        );
    }

    @Transactional
    public Step processUpdateWithScope(Integer id, String desc, String type, Float x, Float y, Float size,
                                       MultipartFile sImg, MultipartFile iImg, String scope, Long routeId) throws Exception {

        Step targetStep;
        Step existingStep = stepRepository.findById(id).orElseThrow();

        if ("local".equalsIgnoreCase(scope) && routeId != null) {
            targetStep = new Step();
            targetStep.setDescription(existingStep.getDescription());
            targetStep.setType(existingStep.getType());
            targetStep.setOverlayX(existingStep.getOverlayX());
            targetStep.setOverlayY(existingStep.getOverlayY());
            targetStep.setIconSize(existingStep.getIconSize());
            targetStep.setImageUrl(existingStep.getImageUrl());
            targetStep.setIconUrl(existingStep.getIconUrl());
        } else {
            targetStep = existingStep;
        }

        if (desc != null) targetStep.setDescription(desc);
        if (type != null) targetStep.setType(type);
        if (x != null) targetStep.setOverlayX(x);
        if (y != null) targetStep.setOverlayY(y);
        if (size != null) targetStep.setIconSize(size);

        if (sImg != null && !sImg.isEmpty()) {
            targetStep.setImageUrl(storageService.uploadAsWebp(sImg, "steps/images"));
        }
        if (iImg != null && !iImg.isEmpty()) {
            targetStep.setIconUrl(storageService.uploadAsWebp(iImg, "steps/icons"));
        }

        Step savedStep = stepRepository.save(targetStep);

        if ("local".equalsIgnoreCase(scope)) {
            routeStepRepository.updateStepLink(routeId, (long) id, (long) savedStep.getId());
        }

        return savedStep;
    }

    @Transactional
    public void processDeleteWithScope(Integer stepId, String scope, Long routeId) {
        if ("global".equalsIgnoreCase(scope)) {
            List<Long> affectedRouteIds = routeStepRepository.findAllRouteIdsByStepId((long) stepId);

            routeStepRepository.deleteByStepId((long) stepId);

            for (Long rId : affectedRouteIds) {
                reorderRouteSteps(rId);
            }

        } else {
            if (routeId == null) throw new RuntimeException("ต้องมี routeId สำหรับการลบแบบ local");

            routeStepRepository.deleteByRouteIdAndStepId(routeId, (long) stepId);

            reorderRouteSteps(routeId);
        }
    }

    @Transactional
    public void addToRoute(Long routeId, Integer stepId, Integer seqOrder, String insertScope) {
        if ("global".equalsIgnoreCase(insertScope)) {
            List<RouteStep> currentSteps = routeStepRepository.findByRouteIdOrderBySeqOrderAsc(routeId);

            if (!currentSteps.isEmpty()) {
                int refIndex = (seqOrder == 1) ? 0 : seqOrder - 2;

                if (refIndex < currentSteps.size()) {
                    RouteStep refStep = currentSteps.get(refIndex);
                    Long refStepId = refStep.getStepId();

                    List<Long> sharedRouteIds = routeStepRepository.findAllRouteIdsByStepId(refStepId);

                    for (Long sharedRouteId : sharedRouteIds) {
                        List<RouteStep> sharedSteps = routeStepRepository.findByRouteIdOrderBySeqOrderAsc(sharedRouteId);

                        for (int i = 0; i < sharedSteps.size(); i++) {
                            if (sharedSteps.get(i).getStepId().equals(refStepId)) {
                                Integer insertPosition = (seqOrder == 1) ? (i + 1) : (i + 2);

                                routeStepRepository.shiftSequence(sharedRouteId, insertPosition);

                                RouteStep newLink = new RouteStep();
                                newLink.setRouteId(sharedRouteId);
                                newLink.setStepId((long) stepId);
                                newLink.setSeqOrder(insertPosition);
                                routeStepRepository.save(newLink);

                                reorderRouteSteps(sharedRouteId);
                                break;
                            }
                        }
                    }
                    return;
                }
            }
        }

        insertLocalOnly(routeId, stepId, seqOrder);
    }

    private void insertLocalOnly(Long routeId, Integer stepId, Integer seqOrder) {
        routeStepRepository.shiftSequence(routeId, seqOrder);

        RouteStep newLink = new RouteStep();
        newLink.setRouteId(routeId);
        newLink.setStepId((long) stepId);
        newLink.setSeqOrder(seqOrder);

        routeStepRepository.save(newLink);
        reorderRouteSteps(routeId);
    }

    private void reorderRouteSteps(Long routeId) {
        List<RouteStep> list = routeStepRepository.findByRouteIdOrderBySeqOrderAsc(routeId);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setSeqOrder(i + 1);
        }
        routeStepRepository.saveAll(list);
    }


    @Transactional
    public Step updateStepImages(Integer id, MultipartFile imageFile, MultipartFile iconFile) throws Exception {

        Step step = stepRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ไม่พบข้อมูลขั้นตอน ID: " + id));

        {
            String imageUrl = storageService.uploadAsWebp(imageFile, "steps/images");
            step.setImageUrl(imageUrl);
        }


        if (iconFile != null && !iconFile.isEmpty()) {
            String iconUrl = storageService.uploadAsWebp(iconFile, "steps/icons");
            step.setIconUrl(iconUrl);
        }


        return stepRepository.save(step);
    }

    public List<String> getUniqueDescriptions() {
        return stepRepository.findDistinctDescriptions();
    }
}
