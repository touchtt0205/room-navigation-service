package com.app.room_navigation_service.controller;

import com.app.room_navigation_service.DTO.BuildingDTO;
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

    @GetMapping()
    public List<BuildingDTO> getAllBuilding() {
        return buildingService.getAllBuildings();
    }

    @GetMapping("/faculty/{facultyId}")
    public List<Building> getBuildingsByFaculty(@PathVariable Integer facultyId) {
        return buildingService.getByFacultyId(facultyId);
    }

    @PostMapping
    public BuildingDTO createBuilding(@RequestBody BuildingDTO building) {
        BuildingDTO w_building = new BuildingDTO();
        w_building.setName(building.getName());
        w_building.setTotalFloor(building.getTotalFloor());
        w_building.setLongitude(building.getLongitude());
        w_building.setLatitude(building.getLatitude());
        w_building.setFacultyId(building.getFacultyId());
        return buildingService.createBuilding(w_building);
    }

    @PutMapping("/{id}")
    public BuildingDTO update(
            @PathVariable Integer id,
            @RequestBody BuildingDTO dto
    ) {
        return buildingService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        buildingService.delete(id);
    }
}
