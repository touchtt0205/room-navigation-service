package com.app.room_navigation_service.controller;

import com.app.room_navigation_service.entity.NavigationLog;
import com.app.room_navigation_service.service.NavigationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/navlog")
@RequiredArgsConstructor
@CrossOrigin
public class NavigationLogController {
    private final NavigationLogService navigationLogService;

    @PostMapping
    public NavigationLog create(@RequestBody NavigationLog dto) {
        return navigationLogService.create(dto);
    }
}
