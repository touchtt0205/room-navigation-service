package com.app.room_navigation_service.controller;
import com.app.room_navigation_service.entity.Building;
import com.app.room_navigation_service.entity.Faculty;
import com.app.room_navigation_service.service.FacultyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping
    public List<Faculty> getAllRooms() {
        return facultyService.getAllFaculty();
    }

    @PostMapping
    public Faculty createRoom(@RequestBody Faculty faculty) {
        return facultyService.createBuilding(faculty);
    }
}
