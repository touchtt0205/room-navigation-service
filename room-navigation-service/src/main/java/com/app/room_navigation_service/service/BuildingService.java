package com.app.room_navigation_service.service;
import com.app.room_navigation_service.DTO.BuildingDTO;
import com.app.room_navigation_service.entity.Building;
import com.app.room_navigation_service.entity.Faculty;
import com.app.room_navigation_service.entity.Room;
import com.app.room_navigation_service.repository.BuildingRepository;
import com.app.room_navigation_service.repository.FacultyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BuildingService {
    private final BuildingRepository buildingRepository;
    private final FacultyRepository facultyRepository;

    public BuildingService(FacultyRepository facultyRepository, BuildingRepository buidingRepository) {
        this.buildingRepository = buidingRepository ;
        this.facultyRepository = facultyRepository;
    }

    public List<BuildingDTO> getAllBuildings() {
        return buildingRepository.findAll().stream()
                .map(building -> {
                    BuildingDTO dto = new BuildingDTO();
                    dto.setId(building.getId());
                    dto.setName(building.getName());
                    dto.setTotalFloor(building.getTotalFloor());
                    dto.setLongitude(building.getLongitude());
                    dto.setLatitude(building.getLatitude());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<Building> getByFacultyId(Integer id) {
        return buildingRepository.findByFaculty_Id(id);
    }

    public Building createBuilding(Building building) {
        return buildingRepository.save(building);
    }
}
