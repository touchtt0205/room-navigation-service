package com.app.room_navigation_service.controller;

import com.app.room_navigation_service.DTO.StepUsageDTO;
import com.app.room_navigation_service.entity.Step;
import com.app.room_navigation_service.service.StepService;
import com.app.room_navigation_service.service.StorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/steps")
public class StepController {

    private final StepService stepService;
    private final StorageService storageService;

    public StepController(StepService stepService, StorageService storageService) {
        this.stepService = stepService;
        this.storageService = storageService;
    }

    // Create
    @PostMapping
    public ResponseEntity<Step> createStep(
//            @RequestParam String name,
            @RequestParam String description,
            @RequestParam String type,
            @RequestParam Float overlayX,
            @RequestParam Float overlayY,
            @RequestParam Float iconSize,
            @RequestParam MultipartFile stepImage,
            @RequestParam MultipartFile iconImage
    ) {
        try {
            Step step = new Step();
//            step.setName(name);
            step.setDescription(description);
            step.setType(type);
            step.setOverlayX(overlayX);
            step.setOverlayY(overlayY);
            step.setIconSize(iconSize);

            // อัปโหลดไฟล์ไป MinIO
            if (stepImage != null && !stepImage.isEmpty()) {
                String stepUrl = storageService.uploadAsWebp(stepImage, "steps/images");
                step.setImageUrl(stepUrl);
            }

            if (iconImage != null && !iconImage.isEmpty()) {
                String iconUrl = storageService.uploadAsWebp(iconImage, "steps/icons");
                step.setIconUrl(iconUrl);
            }

            Step savedStep = stepService.saveStep(step);
            return ResponseEntity.ok(savedStep);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/{id}/add-to-route")
    public ResponseEntity<Void> linkStepToRoute(
            @PathVariable Integer id,
            @RequestParam Long routeId,
            @RequestParam Integer seqOrder,
            @RequestParam(defaultValue = "local") String insertScope
    ) {
        try {
            stepService.addToRoute(routeId, id, seqOrder, insertScope);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Step> updateStep(
            @PathVariable Integer id,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Float overlayX,
            @RequestParam(required = false) Float overlayY,
            @RequestParam(required = false) Float iconSize,
            @RequestParam(value = "stepImage", required = false) MultipartFile stepImage,
            @RequestParam(value = "iconImage", required = false) MultipartFile iconImage,
            @RequestParam(defaultValue = "global") String updateScope,
            @RequestParam(required = false) Long routeId
    ) {
        try {
            Step result = stepService.processUpdateWithScope(
                    id, description, type, overlayX, overlayY, iconSize,
                    stepImage, iconImage, updateScope, routeId
            );

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }


    // Read All
    @GetMapping
    public ResponseEntity<List<Step>> getAllSteps() {
        return ResponseEntity.ok(stepService.getAllSteps());
    }

    // Read One
    @GetMapping("/{id}")
    public ResponseEntity<Step> getStepById(@PathVariable Integer id) {
        return stepService.getStepById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/usage")
    public ResponseEntity<StepUsageDTO> getStepUsage(@PathVariable Long id) {
        return ResponseEntity.ok(stepService.getUsage(id));
    }

    // Update
//    @PutMapping("/{id}")
//    public ResponseEntity<Step> updateStep(@PathVariable Integer id, @RequestBody Step step) {
//        return stepService.getStepById(id)
//                .map(existing -> {
//                    step.setId(id);
//                    Step updated = stepService.saveStep(step);
//                    return ResponseEntity.ok(updated);
//                })
//                .orElse(ResponseEntity.notFound().build());
//    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStep(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "local") String scope,
            @RequestParam(required = false) Long routeId
    ) {
        try {
            stepService.processDeleteWithScope(id, scope, routeId);

            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/{id}/upload-images")
    public ResponseEntity<Step> uploadStepImages(
            @PathVariable Integer id,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "icon", required = false) MultipartFile icon) {

        try {
            Step updatedStep = stepService.updateStepImages(id, image, icon);
            return ResponseEntity.ok(updatedStep);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/descriptions/unique")
    public ResponseEntity<List<String>> getUniqueDescriptions() {
        return ResponseEntity.ok(stepService.getUniqueDescriptions());
    }
}
