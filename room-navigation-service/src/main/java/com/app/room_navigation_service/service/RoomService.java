package com.app.room_navigation_service.service;

import com.app.room_navigation_service.DTO.RoomDTO;
import com.app.room_navigation_service.entity.Building;
import com.app.room_navigation_service.entity.Room;
import com.app.room_navigation_service.repository.RoomRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public RoomDTO getRoomById(Long id) {
        RoomDTO dto = new RoomDTO();
        Room w_room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with id " + id));
        dto.setId(w_room.getId());
        dto.setName(w_room.getName());
        dto.setFloor(w_room.getFloor());
        Building w_building = w_room.getBuilding();

        if (w_building != null) {
            String buildingName = w_building.getName();
            dto.setBuilding(buildingName);
            if (w_building.getFaculty() != null) {
                dto.setFaculty(w_building.getFaculty().getName());
            }
        }
        return dto;
    }

    public List<RoomDTO> searchRooms(String query, Pageable pageable) {
        List<Room> rooms = roomRepository.searchRooms(query, pageable);

        return rooms.stream().map(room -> {
            RoomDTO dto = new RoomDTO();
            dto.setId(room.getId());
            dto.setName(room.getName());
            dto.setFloor(room.getFloor());

            Building w_building = room.getBuilding();

            if (w_building != null) {
                String buildingName = w_building.getName();
                dto.setBuilding(buildingName);
                if (w_building.getFaculty() != null) {
                    dto.setFaculty(w_building.getFaculty().getName());
                }
            }
            return dto;
        }).collect(Collectors.toList());
    }

    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

//    public Room updateRoom(Long id, Room updatedRoom) {
//        Room room = getRoomById(id);
//        room.setName(updatedRoom.getName());
//        room.setDescription(updatedRoom.getDescription());
//        return roomRepository.save(room);
//    }
//
//    public void deleteRoom(Long id) {
//        Room room = getRoomById(id);
//        roomRepository.delete(room);
//    }
//
//    public Room setRoomImage(Long id, String url) {
//        Room room = getRoomById(id);
//        room.setImageUrl(url);
//        return roomRepository.save(room);
//    }

}