package com.app.room_navigation_service.service;
import com.app.room_navigation_service.DTO.BuildingDTO;
import com.app.room_navigation_service.entity.Building;
import com.app.room_navigation_service.entity.Faculty;
import com.app.room_navigation_service.entity.Floor;
import com.app.room_navigation_service.entity.Room;
import com.app.room_navigation_service.repository.BuildingRepository;
import com.app.room_navigation_service.repository.FacultyRepository;
import com.app.room_navigation_service.repository.FloorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BuildingService {
    private final BuildingRepository buildingRepository;
    private final FacultyRepository facultyRepository;
    private final FloorRepository floorRepository;

    public BuildingService(FacultyRepository facultyRepository, BuildingRepository buidingRepository, FloorRepository floorRepository) {
        this.buildingRepository = buidingRepository ;
        this.facultyRepository = facultyRepository;
        this.floorRepository = floorRepository;
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

    public BuildingDTO createBuilding(BuildingDTO dto) {


        Faculty faculty = facultyRepository.findById(Long.valueOf(dto.getFacultyId()))
                .orElseThrow(() -> new RuntimeException("Faculty not found"));

        Building building = new Building();
        building.setName(dto.getName());
        building.setTotalFloor(dto.getTotalFloor());
        building.setLatitude(dto.getLatitude());
        building.setLongitude(dto.getLongitude());
        building.setFaculty(faculty);

        Building savedBuilding = buildingRepository.save(building);

        for (int i = 1; i <= savedBuilding.getTotalFloor(); i++) {
            Floor floor = new Floor();
            floor.setName("ชั้น " + i);
            floor.setRoomCount(0);
            floor.setBuilding(savedBuilding);

            floorRepository.save(floor);
        }

        dto.setId(savedBuilding.getId());
        return dto;
    }

    public BuildingDTO update(Integer id, BuildingDTO dto) {

        Building building = buildingRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Building not found"));

        Faculty faculty = facultyRepository.findById(Long.valueOf(dto.getFacultyId()))
                .orElseThrow(() -> new RuntimeException("Faculty not found"));

        building.setName(dto.getName());
        building.setLatitude(dto.getLatitude());
        building.setLongitude(dto.getLongitude());
        building.setFaculty(faculty);

        if (!building.getTotalFloor().equals(dto.getTotalFloor())) {

            int oldTotal = building.getTotalFloor();
            int newTotal = dto.getTotalFloor();

            building.setTotalFloor(newTotal);
            buildingRepository.save(building);

            if (newTotal > oldTotal) {
                for (int i = oldTotal + 1; i <= newTotal; i++) {
                    Floor floor = new Floor();
                    floor.setName("Floor " + i);
                    floor.setRoomCount(0);
                    floor.setBuilding(building);
                    floorRepository.save(floor);
                }
            }

            if (newTotal < oldTotal) {
                List<Floor> floors = floorRepository.findByBuildingId(id);
                floors.stream()
                        .filter(f -> Integer.parseInt(f.getName().replace("Floor ", "")) > newTotal)
                        .forEach(floorRepository::delete);
            }

        } else {
            buildingRepository.save(building);
        }

        return mapToDTO(building);
    }

    public void delete(Integer id) {
        Building building = buildingRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Building not found"));

        List<Floor> floors = floorRepository.findByBuildingId(id);
        floorRepository.deleteAll(floors);

        buildingRepository.delete(building);
    }

    private BuildingDTO mapToDTO(Building building) {
        BuildingDTO dto = new BuildingDTO();
        dto.setId(building.getId());
        dto.setName(building.getName());
        dto.setTotalFloor(building.getTotalFloor());
        dto.setLatitude(building.getLatitude());
        dto.setLongitude(building.getLongitude());
        dto.setFacultyId(
                building.getFaculty() != null ? building.getFaculty().getId() : null
        );
        return dto;
    }


}
