package com.app.room_navigation_service.controller;

import com.app.room_navigation_service.entity.Building;
import com.app.room_navigation_service.service.BuildingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buildings")
public class BuildingController {
    private final BuildingService buildingService;

    public BuildingController(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    @GetMapping
    public List<Building> getAllBuilding() {
        return buildingService.getAllBuildings();
    }

    @GetMapping("/faculty/{facultyId}")
    public List<Building> getBuildingsByFaculty(@PathVariable Integer facultyId) {
        return buildingService.getByFacultyId(facultyId);
    }

    @PostMapping
    public Building createBuilding(@RequestBody Building building) {
        return buildingService.createBuilding(building, building.getFaculty().getId());
    }
}
