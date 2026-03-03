package com.app.room_navigation_service.repository;

import com.app.room_navigation_service.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RouteRepository extends JpaRepository<Route, Integer> {
    List<Route> findByRoomId(Long roomId);

    @Transactional
    void deleteByRoomId(Long roomId);
}