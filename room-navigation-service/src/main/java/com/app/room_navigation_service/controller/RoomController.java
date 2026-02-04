//package com.app.room_navigation_service.controller;
//
//import com.app.room_navigation_service.entity.Room;
//import com.app.room_navigation_service.service.RoomService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.ExampleObject;
//import io.swagger.v3.oas.annotations.media.Schema;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import com.app.room_navigation_service.service.StorageService;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/rooms")
//@CrossOrigin(origins = "http://localhost:5173") // React dev
//public class RoomController {
//
//    private final RoomService roomService;
//    private final StorageService storageService;
//
//    public RoomController(RoomService roomService, StorageService storageService) {
//        this.roomService = roomService;
//        this.storageService = storageService;
//    }
//
//    @GetMapping
//    public List<Room> getAllRooms() {
//        return roomService.getAllRooms();
//    }
//
//    @GetMapping("/{id}")
//    public Room getRoom(@PathVariable Long id) {
//        return roomService.getRoomById(id);
//    }
//
//    @PostMapping
//    @io.swagger.v3.oas.annotations.parameters.RequestBody(
//            description = "Room info",
//            required = true,
//            content = @Content(
//                    mediaType = "application/json",
//                    schema = @Schema(implementation = Room.class),
//                    examples = @ExampleObject(
//                            value = "{ \"name\": \"Room A\", \"description\": \"Nice room\", \"imageUrl\": \"\" }"
//                    )
//            )
//    )
//    public Room createRoom(@RequestBody Room room) {
//        return roomService.createRoom(room);
//    }
//
//    @PostMapping("/{id}/upload")
//    @Operation(summary = "Upload room image")
//    public Room uploadRoomImage(
//            @PathVariable Long id,
//            @RequestPart("file") MultipartFile file  // <-- ใช้ @RequestPart แทน @RequestParam
//    ) throws Exception {
//        String objectName = "room-" + id + "-" + file.getOriginalFilename();
//        String url = storageService.uploadFile(file, objectName);
//        System.out.println("Uploaded URL: " + url);
//
//        return roomService.setRoomImage(id, url);
//    }
//
//}