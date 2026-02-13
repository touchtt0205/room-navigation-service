package com.app.room_navigation_service.repository;

import com.app.room_navigation_service.entity.RouteStep;
import com.app.room_navigation_service.entity.Step;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteStepRepository extends JpaRepository<RouteStep, Long> {
    @Query("SELECT rs.step FROM RouteStep rs " +
            "WHERE rs.route.id = :routeId " +
            "ORDER BY rs.seqOrder ASC")
    List<Step> findStepsByRouteId(@Param("routeId") Long routeId);
}
