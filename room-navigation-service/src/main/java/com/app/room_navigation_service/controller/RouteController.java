package com.app.room_navigation_service.controller;

import com.app.room_navigation_service.DTO.StepDTO;
import com.app.room_navigation_service.service.RouteStepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
@CrossOrigin(origins = "http://localhost:5173")
public class RouteController {

    @Autowired
    private RouteStepService routeStepService;

    @GetMapping("/{routeId}/steps")
    public List<StepDTO> getSteps(@PathVariable Long routeId) {
        return routeStepService.getStepsByRouteId(routeId);
    }
}