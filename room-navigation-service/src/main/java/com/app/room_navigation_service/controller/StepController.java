package com.app.room_navigation_service.controller;

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

    public StepController(StepService stepService ,StorageService storageService) {
        this.stepService = stepService;
        this.storageService = storageService;
    }

    // Create
    @PostMapping
    public ResponseEntity<Step> createStep(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam String type,
            @RequestParam Float overlayX,
            @RequestParam Float overlayY,
            @RequestParam MultipartFile stepImage,
            @RequestParam MultipartFile iconImage
    ) {
        try {
            Step step = new Step();
            step.setName(name);
            step.setDescription(description);
            step.setType(type);
            step.setOverlayX(overlayX);
            step.setOverlayY(overlayY);

            // อัปโหลดไฟล์ไป MinIO
            String stepUrl = storageService.uploadFile(stepImage, "step");
            String iconUrl = storageService.uploadFile(iconImage, "icon");

            step.setImageUrl(stepUrl);
            step.setIconUrl(iconUrl);

            Step savedStep = stepService.saveStep(step);
            return ResponseEntity.ok(savedStep);
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

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<Step> updateStep(@PathVariable Integer id, @RequestBody Step step) {
        return stepService.getStepById(id)
                .map(existing -> {
                    step.setId(id);
                    Step updated = stepService.saveStep(step);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStep(@PathVariable Integer id) {
        if (stepService.getStepById(id).isPresent()) {
            stepService.deleteStep(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
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
}
