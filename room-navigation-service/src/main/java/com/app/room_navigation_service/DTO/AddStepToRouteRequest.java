package com.app.room_navigation_service.DTO;

import lombok.Data;

@Data
public class AddStepToRouteRequest {
    private Long stepId;
    private Integer seqOrder;
}