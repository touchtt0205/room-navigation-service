package com.app.room_navigation_service.service;
//
//import com.app.room_navigation_service.DTO.RoomDTO;
//import com.app.room_navigation_service.entity.Building;
//import com.app.room_navigation_service.entity.Room;
//import com.app.room_navigation_service.entity.Route;
//import com.app.room_navigation_service.entity.RouteStep;
//import com.app.room_navigation_service.repository.RoomRepository;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import java.util.Comparator;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class RoomService {
//
//    private final RoomRepository roomRepository;
//
//    public RoomService(RoomRepository roomRepository) {
//        this.roomRepository = roomRepository;
//    }
//
//    public List<Room> getAllRooms() {
//        return roomRepository.findAll();
//    }
//
//    public RoomDTO getRoomById(Long id) {
//        RoomDTO dto = new RoomDTO();
//        Room w_room = roomRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Room not found with id " + id));
//
//        dto.setId(w_room.getId());
//        dto.setName(w_room.getName());
//        dto.setFloor(w_room.getFloor());
//
//        if (w_room.getRoutes() != null && !w_room.getRoutes().isEmpty()) {
//            Route route = w_room.getRoutes().get(0);
//            dto.setRouteId(route.getId());
//
//            route.getRouteSteps().stream()
//                    .max(Comparator.comparing(RouteStep::getSeqOrder))
//                    .ifPresent(lastRouteStep -> {
//                        if (lastRouteStep.getStep() != null) {
//                            dto.setRoom_image_url(lastRouteStep.getStep().getImageUrl());
//                        }
//                    });
//        }
//
//        Building w_building = w_room.getBuilding();
//        if (w_building != null) {
//            dto.setBuilding(w_building.getName());
//            if (w_building.getFaculty() != null) {
//                dto.setFaculty(w_building.getFaculty().getName());
//            }
//        }
//
//        return dto;
//    }
//
//    public List<RoomDTO> searchRooms(String query, Pageable pageable) {
//        List<Room> rooms = roomRepository.searchRooms(query, pageable);
//
//        return rooms.stream().map(room -> {
//            RoomDTO dto = new RoomDTO();
//            dto.setId(room.getId());
//            dto.setName(room.getName());
//            dto.setFloor(room.getFloor());
//
//            Building w_building = room.getBuilding();
//
//            if (w_building != null) {
//                String buildingName = w_building.getName();
//                dto.setBuilding(buildingName);
//                if (w_building.getFaculty() != null) {
//                    dto.setFaculty(w_building.getFaculty().getName());
//                }
//            }
//            return dto;
//        }).collect(Collectors.toList());
//    }
//
//    public Room createRoom(Room room) {
//        return roomRepository.save(room);
//    }
//

import com.app.room_navigation_service.DTO.RoomDTO;
import com.app.room_navigation_service.entity.Floor;
import com.app.room_navigation_service.entity.Room;
import com.app.room_navigation_service.repository.FloorRepository;
import com.app.room_navigation_service.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

////    public Room updateRoom(Long id, Room updatedRoom) {
////        Room room = getRoomById(id);
////        room.setName(updatedRoom.getName());
////        room.setDescription(updatedRoom.getDescription());
////        return roomRepository.save(room);
////    }
////
////    public void deleteRoom(Long id) {
////        Room room = getRoomById(id);
////        roomRepository.delete(room);
////    }
////
////    public Room setRoomImage(Long id, String url) {
////        Room room = getRoomById(id);
////        room.setImageUrl(url);
////        return roomRepository.save(room);
////    }
//
//}

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final FloorRepository floorRepository;

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

    public void delete(Integer id) {
        if (!roomRepository.existsById(id)) {
            throw new RuntimeException("Room not found");
        }
        roomRepository.deleteById(id);
    }

    public List<RoomDTO> getByFloor(Integer floorId) {
        return roomRepository.findByFloorIdOrderByIdAsc(floorId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public RoomDTO getById(Integer id) {
        return roomRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Room not found"));
    }

    private RoomDTO mapToDTO(Room room) {
        RoomDTO dto = new RoomDTO();
        dto.setId(room.getId());
        dto.setName(room.getName());
        dto.setFloorId(room.getFloor().getId());
        dto.setHasSharedRoute(room.getHasSharedRoute());
        return dto;
    }
}
