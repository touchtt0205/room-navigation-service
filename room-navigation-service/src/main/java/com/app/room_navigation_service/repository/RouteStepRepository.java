package com.app.room_navigation_service.repository;

import com.app.room_navigation_service.DTO.RouteStepDetailDTO;
import com.app.room_navigation_service.entity.RouteStep;
import com.app.room_navigation_service.entity.Step;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteStepRepository extends JpaRepository<RouteStep, Long> {
//    @Query("SELECT rs.step FROM RouteStep rs " +
//            "WHERE rs.route.id = :routeId " +
//            "ORDER BY rs.seqOrder ASC")
//    List<Step> findStepsByRouteId(@Param("routeId") Long routeId);
    @Query("SELECT s FROM Step s JOIN RouteStep rs ON s.id = rs.stepId WHERE rs.routeId = :routeId ORDER BY rs.seqOrder ASC")
    List<Step> findStepsByRouteId(@Param("routeId") Long routeId);
    List<RouteStep> findByRouteIdOrderBySeqOrder(Long routeId);

    @Query("""
    SELECT new com.app.room_navigation_service.DTO.RouteStepDetailDTO(
        s.id,
        rs.seqOrder,
        s.name,
        s.description,
        s.imageUrl,
        s.iconUrl,
        s.overlayX,
        s.overlayY,
        s.iconSize,
        s.type
    )
    FROM RouteStep rs
    JOIN Step s ON rs.stepId = s.id
    WHERE rs.routeId = :routeId
    ORDER BY rs.seqOrder
""")
    List<RouteStepDetailDTO> findRouteStepDetailsByRouteId(@Param("routeId") Long routeId);
}
