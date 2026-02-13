package com.app.room_navigation_service.service;

import com.app.room_navigation_service.DTO.StepDTO;
import com.app.room_navigation_service.entity.Step;
import com.app.room_navigation_service.repository.RouteStepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RouteStepService {

    @Autowired
    private RouteStepRepository routeStepRepository;

    public List<StepDTO> getStepsByRouteId(Long routeId) {
        List<Step> steps = routeStepRepository.findStepsByRouteId(routeId);

        return steps.stream().map(step -> new StepDTO(
                step.getId(),
                step.getName(),
                step.getDescription(),
                step.getImageUrl(),
                step.getIconUrl(),
                step.getOverlayX(),
                step.getOverlayY(),
                step.getType()
        )).collect(Collectors.toList());
    }
}