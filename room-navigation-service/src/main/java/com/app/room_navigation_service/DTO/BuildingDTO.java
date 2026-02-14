package com.app.room_navigation_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuildingDTO {
    private Integer id;
    private String name;
    private Integer totalFloor;
    private Double latitude;
    private Double longitude;
}
