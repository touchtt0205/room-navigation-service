package com.app.room_navigation_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StepDTO {
    private Integer id;
    private String name;
    private String description;
    private String imageUrl;
    private String iconUrl;
    private Float overlayX;
    private Float overlayY;
    private String type;
}