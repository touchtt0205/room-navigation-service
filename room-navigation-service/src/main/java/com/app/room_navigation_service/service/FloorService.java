package com.app.room_navigation_service.service;

import com.app.room_navigation_service.DTO.FloorDTO;
import com.app.room_navigation_service.entity.Building;
import com.app.room_navigation_service.entity.Floor;
import com.app.room_navigation_service.repository.BuildingRepository;
import com.app.room_navigation_service.repository.FloorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FloorService {

    private final FloorRepository floorRepository;
    private final BuildingRepository buildingRepository;

    public FloorDTO create(FloorDTO dto) {

        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new IllegalArgumentException("Floor name is required");
        }

        if (dto.getRoomCount() != null && dto.getRoomCount() < 0) {
            throw new IllegalArgumentException("Room count cannot be negative");
        }

        Building building = buildingRepository.findById(Long.valueOf(dto.getBuildingId()))
                .orElseThrow(() -> new RuntimeException("Building not found"));

        Floor floor = new Floor();
        floor.setName(dto.getName());
        floor.setRoomCount(dto.getRoomCount());
        floor.setBuilding(building);

        Floor saved = floorRepository.save(floor);

        return mapToDTO(saved);
    }

    public List<FloorDTO> getByBuilding(Integer buildingId) {
        return floorRepository.findByBuildingId(buildingId)
                .stream()
                .sorted(Comparator.comparing(Floor::getId))
                .map(this::mapToDTO)
                .toList();
    }

    public FloorDTO update(Integer id, FloorDTO dto) {

        Floor floor = floorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Floor not found"));

        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new IllegalArgumentException("Floor name is required");
        }

        if (dto.getRoomCount() != null && dto.getRoomCount() < 0) {
            throw new IllegalArgumentException("Room count cannot be negative");
        }

        floor.setName(dto.getName());
        floor.setRoomCount(dto.getRoomCount());

        Floor saved = floorRepository.save(floor);

        return mapToDTO(saved);
    }

    public void delete(Integer id) {
        Floor floor = floorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Floor not found"));

        floorRepository.delete(floor);
    }

    private FloorDTO mapToDTO(Floor floor) {
        FloorDTO dto = new FloorDTO();
        dto.setId(floor.getId());
        dto.setName(floor.getName());
        dto.setRoomCount(floor.getRoomCount());
        dto.setBuildingId(floor.getBuilding().getId());
        return dto;
    }
}
