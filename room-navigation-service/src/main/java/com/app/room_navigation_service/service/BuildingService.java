package com.app.room_navigation_service.service;
import com.app.room_navigation_service.entity.Building;
import com.app.room_navigation_service.entity.Faculty;
import com.app.room_navigation_service.entity.Room;
import com.app.room_navigation_service.repository.BuildingRepository;
import com.app.room_navigation_service.repository.FacultyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuildingService {
    private final BuildingRepository buildingRepository;
    private final FacultyRepository facultyRepository;

    public BuildingService(FacultyRepository facultyRepository, BuildingRepository buidingRepository) {
        this.buildingRepository = buidingRepository ;
        this.facultyRepository = facultyRepository;
    }

    public List<Building> getAllBuildings() {
        return buildingRepository.findAll();
    }

    public List<Building> getByFacultyId(Integer id) {
        return buildingRepository.findByFaculty_Id(id);
    }

//    public Building createBuilding(Building building) {
//        return buildingRepository.save(building);
//    }

    public Building createBuilding(Building building, Integer  facultyId) {
        Faculty faculty = facultyRepository.findById(Long.valueOf(facultyId))
                .orElseThrow(() -> new RuntimeException("Faculty not found"));
        building.setFaculty(faculty);
        return buildingRepository.save(building);
    }
}
