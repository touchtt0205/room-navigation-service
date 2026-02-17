package com.app.room_navigation_service.controller;

import com.app.room_navigation_service.DTO.FloorDTO;
import com.app.room_navigation_service.service.FloorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/floors")
@RequiredArgsConstructor
@CrossOrigin
public class FloorController {

    private final FloorService floorService;

    @PostMapping
    public FloorDTO create(@RequestBody FloorDTO dto) {
        return floorService.create(dto);
    }

    @GetMapping("/{buildingId}")
    public List<FloorDTO> getByBuilding(@PathVariable Integer buildingId) {
        return floorService.getByBuilding(buildingId);
    }

    @PutMapping("/{id}")
    public FloorDTO update(@PathVariable Integer id, @RequestBody FloorDTO dto) {
        return floorService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        floorService.delete(id);
    }
}
