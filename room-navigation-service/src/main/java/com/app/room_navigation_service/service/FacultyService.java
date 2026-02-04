package com.app.room_navigation_service.service;

import com.app.room_navigation_service.entity.Building;
import com.app.room_navigation_service.entity.Faculty;
import com.app.room_navigation_service.repository.FacultyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public List<Faculty> getAllFaculty() {
        return facultyRepository.findAll();
    }

    public Faculty createBuilding(Faculty faculty) {
        return facultyRepository.save(faculty);
    }
}
