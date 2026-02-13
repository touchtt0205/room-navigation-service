package com.app.room_navigation_service.service;

import com.app.room_navigation_service.entity.Step;
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
}
