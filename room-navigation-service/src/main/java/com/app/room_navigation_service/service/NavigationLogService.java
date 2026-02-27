package com.app.room_navigation_service.service;

import com.app.room_navigation_service.entity.NavigationLog;
import com.app.room_navigation_service.repository.NavigationLogRepository;
import org.springframework.stereotype.Service;

@Service
public class NavigationLogService {
    private final NavigationLogRepository navigationLogRepository;

    public NavigationLogService(NavigationLogRepository navigationLogRepository) {
        this.navigationLogRepository = navigationLogRepository;
    }

    public NavigationLog create(NavigationLog navLog){
        return navigationLogRepository.save(navLog);
    }
}
