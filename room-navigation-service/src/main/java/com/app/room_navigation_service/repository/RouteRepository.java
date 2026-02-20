package com.app.room_navigation_service.repository;

import com.app.room_navigation_service.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteRepository extends JpaRepository<Route, Long> {
    List<Route> findByRoomId(Long roomId);
}