package com.app.room_navigation_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RouteDTO {
    private Long id;
    private String name;
    private String startFrom;
    private Long roomId;
}