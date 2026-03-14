package com.app.room_navigation_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StepUsageDTO {
    private Boolean isShared;
    private Long usageCount;
    private List<String> routeNames;

}
