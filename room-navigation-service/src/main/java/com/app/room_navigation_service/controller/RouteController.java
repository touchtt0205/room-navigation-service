package com.app.room_navigation_service.controller;

import com.app.room_navigation_service.DTO.RouteStepDetailDTO;
import com.app.room_navigation_service.DTO.RouteStepsDTO;
import com.app.room_navigation_service.DTO.StepDTO;
import com.app.room_navigation_service.entity.Route;
import com.app.room_navigation_service.entity.RouteStep;
import com.app.room_navigation_service.service.RouteService;
import com.app.room_navigation_service.service.RouteStepService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    private final RouteService routeService;
    private final RouteStepService routeStepService;

    public RouteController(RouteService routeService, RouteStepService routeStepService) {
        this.routeService = routeService;
        this.routeStepService = routeStepService;
    }

    // Create
    @PostMapping
    public ResponseEntity<Route> createRoute(@RequestBody Route route) {
        return ResponseEntity.ok(routeService.createRoute(route));
    }

    // Read All
    @GetMapping
    public ResponseEntity<List<Route>> getAllRoutes() {
        return ResponseEntity.ok(routeService.getAllRoutes());
    }

    // Read by ID
    @GetMapping("/{routeId}")
    public ResponseEntity<Route> getRouteById(@PathVariable Long routeId) {
        return routeService.getRouteById(routeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Read by RoomId
    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<Route>> getRoutesByRoom(@PathVariable Long roomId) {
        return ResponseEntity.ok(routeService.getRoutesByRoomId(roomId));
    }

    // Read Steps by RouteId
    @GetMapping("/{routeId}/steps")
    public ResponseEntity<List<StepDTO>> getSteps(@PathVariable Long routeId) {
        return ResponseEntity.ok(routeStepService.getStepsByRouteId(routeId));
    }

    // Read RouteSteps (stepId + seqOrder) by RouteId
    @GetMapping("/{routeId}/routesteps")
    public ResponseEntity<List<RouteStepsDTO>> getRouteSteps(@PathVariable Long routeId) {
        return ResponseEntity.ok(
                routeStepService.getRouteStepsDTOByRouteId(routeId)
        );
    }

    @GetMapping("/{routeId}/step-details")
    public ResponseEntity<List<RouteStepDetailDTO>> getRouteStepDetails(
            @PathVariable Long routeId) {
        return ResponseEntity.ok(
                routeStepService.getRouteStepDetails(routeId)
        );
    }

    // Update
    @PutMapping("/{routeId}")
    public ResponseEntity<Route> updateRoute(@PathVariable Long routeId, @RequestBody Route route) {
        try {
            return ResponseEntity.ok(routeService.updateRoute(routeId, route));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete
    @DeleteMapping("/{routeId}")
    public ResponseEntity<Void> deleteRoute(@PathVariable Long routeId) {
        routeService.deleteRoute(routeId);
        return ResponseEntity.ok().build();
    }

    // Add Step to Route
    @PostMapping("/{routeId}/steps")
    public ResponseEntity<RouteStep> addStepToRoute(
            @PathVariable Long routeId,
            @RequestBody RouteStepsDTO request) {
        return ResponseEntity.ok(routeStepService.addStepToRoute(routeId, request.getStepId(), request.getSeqOrder()));
    }

    // Remove Step from Route
    @DeleteMapping("/{routeId}/steps/{stepId}")
    public ResponseEntity<Void> removeStepFromRoute(
            @PathVariable Long routeId,
            @PathVariable Long stepId) {
        routeStepService.removeStepFromRoute(routeId, stepId);
        return ResponseEntity.ok().build();
    }

//    @GetMapping("/{routeId}/steps")
//    public List<StepDTO> getSteps(@PathVariable Long routeId) {
//        return routeStepService.getStepsByRouteId(routeId);
//    }
}