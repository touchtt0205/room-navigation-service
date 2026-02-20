package com.app.room_navigation_service.service;

import com.app.room_navigation_service.DTO.RouteStepDetailDTO;
import com.app.room_navigation_service.DTO.RouteStepsDTO;
import com.app.room_navigation_service.DTO.StepDTO;
import com.app.room_navigation_service.entity.RouteStep;
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

    public RouteStepService(RouteStepRepository routeStepRepository) {
        this.routeStepRepository = routeStepRepository;
    }

    // Read Steps by RouteId
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

    // Get RouteSteps DTO by RouteId (ดึงจาก route_steps ตรง ๆ)
    public List<RouteStepsDTO> getRouteStepsDTOByRouteId(Long routeId) {
        return routeStepRepository.findByRouteIdOrderBySeqOrder(routeId)
                .stream()
                .map(rs -> new RouteStepsDTO(
                        rs.getStepId(),
                        rs.getSeqOrder()
                ))
                .toList();
    }

    public List<RouteStepDetailDTO> getRouteStepDetails(Long routeId) {
        return routeStepRepository.findRouteStepDetailsByRouteId(routeId);
    }

    // Add Step to Route
    public RouteStep addStepToRoute(Long routeId, Long stepId, Integer seqOrder) {
        RouteStep routeStep = new RouteStep();
        routeStep.setRouteId(routeId);
        routeStep.setStepId(stepId);
        routeStep.setSeqOrder(seqOrder);
        return routeStepRepository.save(routeStep);
    }

    // Remove Step from Route
    public void removeStepFromRoute(Long routeId, Long stepId) {
        routeStepRepository.findByRouteIdOrderBySeqOrder(routeId)
                .stream()
                .filter(rs -> rs.getStepId().equals(stepId))
                .findFirst()
                .ifPresent(routeStepRepository::delete);
    }

    // Get all RouteSteps by RouteId (raw)
    public List<RouteStep> getRouteStepsByRouteId(Long routeId) {
        return routeStepRepository.findByRouteIdOrderBySeqOrder(routeId);
    }

    // Delete all steps in a route (ใช้ตอนลบ route)
    public void deleteAllByRouteId(Long routeId) {
        List<RouteStep> list = routeStepRepository.findByRouteIdOrderBySeqOrder(routeId);
        routeStepRepository.deleteAll(list);
    }
//    public List<StepDTO> getStepsByRouteId(Long routeId) {
//        List<Step> steps = routeStepRepository.findStepsByRouteId(routeId);
//
//        return steps.stream().map(step -> new StepDTO(
//                step.getId(),
//                step.getName(),
//                step.getDescription(),
//                step.getImageUrl(),
//                step.getIconUrl(),
//                step.getOverlayX(),
//                step.getOverlayY(),
//                step.getType()
//        )).collect(Collectors.toList());
//    }
}