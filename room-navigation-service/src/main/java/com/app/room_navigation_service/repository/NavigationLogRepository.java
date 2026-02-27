package com.app.room_navigation_service.repository;

import com.app.room_navigation_service.entity.NavigationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NavigationLogRepository extends JpaRepository<NavigationLog, Integer> {
}
