package com.app.room_navigation_service.DTO;

import com.app.room_navigation_service.entity.Step;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteVisualizationDTO {
    private String routeId;
    private String roomId;
    private String roomName;
    private List<RouteStepDetailDTO> steps;
}
