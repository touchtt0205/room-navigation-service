package com.app.room_navigation_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDTO {
    private Long id;
    private String name;
    private Integer step;
    private String building;
    private Integer floor;
    private String faculty;
}