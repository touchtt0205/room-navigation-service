package com.app.room_navigation_service.controller;


import com.app.room_navigation_service.DTO.RoomDTO;
import com.app.room_navigation_service.DTO.RoomResponseDTO;
import com.app.room_navigation_service.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
@CrossOrigin
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public RoomDTO create(@RequestBody RoomDTO dto) {
        return roomService.create(dto);
    }

    @PutMapping("/{id}")
    public RoomDTO update(@PathVariable Integer id, @RequestBody RoomDTO dto) {
        return roomService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        roomService.delete(id);
    }

    @GetMapping("/floor/{floorId}")
    public List<RoomDTO> getByFloor(@PathVariable Integer floorId) {
        return roomService.getByFloor(floorId);
    }

    @GetMapping("/{id}")
    public RoomDTO getById(@PathVariable Integer id) {
        return roomService.getById(id);
    }

    @GetMapping("/{id}/details")
    public RoomResponseDTO getDetails(@PathVariable Integer id) {
        return roomService.getDetails(id);
    }

    @GetMapping("/search")
    public ResponseEntity<List<RoomResponseDTO>> searchRooms(
            @RequestParam("q") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        List<RoomResponseDTO> results = roomService.searchRooms(query, pageable);

        return ResponseEntity.ok(results);
    }
}
