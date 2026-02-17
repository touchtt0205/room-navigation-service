package com.app.room_navigation_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FloorDTO {
    private Integer id;
    private String name;
    private Integer roomCount;
    private Integer buildingId;
}
