package com.app.room_navigation_service.service;
import com.app.room_navigation_service.DTO.RoomDTO;
import com.app.room_navigation_service.DTO.RoomResponseDTO;
import com.app.room_navigation_service.DTO.RouteStepDetailDTO;
import com.app.room_navigation_service.DTO.RouteVisualizationDTO;
import com.app.room_navigation_service.entity.Floor;
import com.app.room_navigation_service.entity.Room;
import com.app.room_navigation_service.entity.Route;
import com.app.room_navigation_service.repository.FloorRepository;
import com.app.room_navigation_service.repository.RoomRepository;
import com.app.room_navigation_service.repository.RouteRepository;
import com.app.room_navigation_service.repository.RouteStepRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final FloorRepository floorRepository;
    private final RouteRepository routeRepository;
    private final RouteStepRepository routeStepRepository;
    private final RouteStepService routeStepService;

    public RoomDTO create(RoomDTO dto) {
        Floor floor = floorRepository.findById(dto.getFloorId())
                .orElseThrow(() -> new RuntimeException("Floor not found"));

        Room room = Room.builder()
                .name(dto.getName())
                .floor(floor)
                .hasSharedRoute(Boolean.TRUE.equals(dto.getHasSharedRoute()))
                .build();

        return mapToDTO(roomRepository.save(room));
    }

    public RoomDTO update(Integer id, RoomDTO dto) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        room.setName(dto.getName());
        room.setHasSharedRoute(Boolean.TRUE.equals(dto.getHasSharedRoute()));

        return mapToDTO(roomRepository.save(room));
    }

    @Transactional
    public void delete(Integer id) {
        if (!roomRepository.existsById(id)) {
            throw new RuntimeException("Room not found");
        }
        List<Route> routes = routeRepository.findByRoomId(id.longValue());

        for (Route route : routes) {
            routeStepService.deleteAllByRouteId(route.getId().longValue());
        }

        routeRepository.deleteByRoomId(id.longValue());

        roomRepository.deleteById(id);
    }

    public List<RoomDTO> getByFloor(Integer floorId) {
        return roomRepository.findByFloorIdOrderByIdAsc(floorId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public List<RouteVisualizationDTO> getVisualByFloor(Integer floorId) {
        List<Room> rooms = roomRepository.findByFloorIdOrderByIdAsc(floorId);

        return rooms.stream()
                .flatMap(room -> {
                    List<Route> routes = routeRepository.findByRoomId(room.getId().longValue());

                    return routes.stream().map(route -> {
                        RouteVisualizationDTO dto = new RouteVisualizationDTO();
                        dto.setRouteId(route.getId().toString());
                        dto.setRoomId(room.getId().toString());
                        dto.setRoomName(room.getName());

                        List<RouteStepDetailDTO> stepDetails = routeStepRepository
                                .findRouteStepDetailsByRouteId(route.getId().longValue());

                        dto.setSteps(stepDetails);
                        return dto;
                    });
                })
                .toList();
    }

    public RoomDTO getById(Integer id) {
        return roomRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Room not found"));
    }

    public RoomResponseDTO getDetails(Integer id){
        return roomRepository.findById(id)
                .map(this::convertToDetails)
                .orElseThrow(() -> new RuntimeException("Room not found"));
    }

    public List<RoomResponseDTO> searchRooms(String query, Pageable pageable) {
        List<Room> rooms = roomRepository.searchRooms(query, pageable);

        return rooms.stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    private RoomResponseDTO convertToResponseDTO(Room room) {
        RoomResponseDTO dto = new RoomResponseDTO();

        dto.setId(room.getId());
        dto.setName(room.getName());

        if (room.getFloor() != null) {
            dto.setFloor(room.getFloor().getName());

            if (room.getFloor().getBuilding() != null) {
                dto.setBuilding(room.getFloor().getBuilding().getName());
                dto.setLatitude(room.getFloor().getBuilding().getLatitude());
                dto.setLongitude(room.getFloor().getBuilding().getLongitude());
                if (room.getFloor().getBuilding().getFaculty() != null) {
                    dto.setFaculty(room.getFloor().getBuilding().getFaculty().getName());
                }
            }
        }
        return dto;
    }

    private RoomResponseDTO convertToDetails(Room room) {
        RoomResponseDTO dto = this.convertToResponseDTO(room);
        List<Route> routes = routeRepository.findByRoomId(room.getId().longValue());


        if (!routes.isEmpty()) {
            Integer route_id = routes.get(0).getId();

            List<RouteStepDetailDTO> routeSteps = routeStepRepository.findRouteStepDetailsByRouteId(route_id.longValue());

            if (!routeSteps.isEmpty()) {
                dto.setRouteId(route_id.toString());
                dto.setRoom_image_url(routeSteps.get(routeSteps.size() - 1).getImageUrl());
                dto.setSteps(routeSteps);
            }
        }

        return dto;
    }

    private RoomDTO mapToDTO(Room room) {
        RoomDTO dto = new RoomDTO();
        dto.setId(room.getId());
        dto.setName(room.getName());
        dto.setFloorId(room.getFloor().getId());
        dto.setHasSharedRoute(room.getHasSharedRoute());
        Integer stepCount = routeStepRepository.countStepsByRoomId(room.getId());
        dto.setStepCount(stepCount != null ? stepCount : 0);
        return dto;
    }
}
