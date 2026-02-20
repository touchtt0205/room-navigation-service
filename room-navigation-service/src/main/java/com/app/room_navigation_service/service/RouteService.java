package com.app.room_navigation_service.service;

import com.app.room_navigation_service.entity.Route;
import com.app.room_navigation_service.repository.RouteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RouteService {

    private final RouteRepository routeRepository;

    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    // Create
    public Route createRoute(Route route) {
        return routeRepository.save(route);
    }

    // Read All
    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    // Read by ID
    public Optional<Route> getRouteById(Long id) {
        return routeRepository.findById(id);
    }

    // Read by RoomId
    public List<Route> getRoutesByRoomId(Long roomId) {
        return routeRepository.findByRoomId(roomId);
    }

    // Update
    public Route updateRoute(Long id, Route updated) {
        return routeRepository.findById(id).map(existing -> {
            existing.setName(updated.getName());
            existing.setStartFrom(updated.getStartFrom());
            existing.setRoomId(updated.getRoomId());
            return routeRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("ไม่พบ route id: " + id));
    }

    // Delete
    public void deleteRoute(Long id) {
        routeRepository.deleteById(id);
    }
}