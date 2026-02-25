package com.app.room_navigation_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class RoomResponseDTO {
    private Integer id;
    private String name;
    private Integer floor;
    private String building;
    private String faculty;
    private String room_image_url;
    private String routeId;
    private List<RouteStepDetailDTO> steps;
}
