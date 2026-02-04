package com.app.room_navigation_service.service;

import com.app.room_navigation_service.entity.Step;
import com.app.room_navigation_service.repository.StepRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StepService {

    private final StepRepository stepRepository;

    public StepService(StepRepository stepRepository) {
        this.stepRepository = stepRepository;
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
}
