package com.app.room_navigation_service.DTO;

import lombok.Data;

@Data
public class CreateRouteRequest {
    private String name;
    private String startFrom;
    private Long roomId;
}