package com.app.room_navigation_service.repository;

import com.app.room_navigation_service.DTO.RouteStepDetailDTO;
import com.app.room_navigation_service.entity.RouteStep;
import com.app.room_navigation_service.entity.Step;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Query("SELECT COUNT(DISTINCT rs.routeId) FROM RouteStep rs WHERE rs.stepId = :stepId")
    long countRoutesUsingStep(@Param("stepId") Long stepId);

    @Query("SELECT rs.routeId FROM RouteStep rs WHERE rs.stepId = :stepId")
    List<Long> findAllRouteIdsByStepId(@Param("stepId") Long stepId);

    List<RouteStep> findAllByStepId(Long stepId);

    void deleteByStepId(Long stepId);

    @Modifying
    @Query("DELETE FROM RouteStep rs WHERE rs.routeId = :routeId AND rs.stepId = :stepId")
    void deleteByRouteIdAndStepId(@Param("routeId") Long routeId, @Param("stepId") Long stepId);

    List<RouteStep> findByRouteIdOrderBySeqOrderAsc(Long routeId);

    @Query("SELECT r.name FROM Route rt " +
            "JOIN Room r ON rt.roomId = r.id " +
            "JOIN RouteStep rs ON rs.routeId = rt.id " +
            "WHERE rs.stepId = :stepId")
    List<String> findRoomNamesByStepId(@Param("stepId") Long stepId);

    @Modifying
    @Query("UPDATE RouteStep rs SET rs.stepId = :newStepId WHERE rs.routeId = :routeId AND rs.stepId = :oldStepId")
    void updateStepLink(@Param("routeId") Long routeId, @Param("oldStepId") Long oldStepId, @Param("newStepId") Long newStepId);

    @Modifying
    @Query("UPDATE RouteStep rs SET rs.seqOrder = rs.seqOrder + 1 " +
            "WHERE rs.routeId = :routeId AND rs.seqOrder >= :targetOrder")
    void shiftSequence(@Param("routeId") Long routeId, @Param("targetOrder") Integer targetOrder);

    @Query(value = "SELECT COUNT(rs.id) FROM route_steps rs " +
            "JOIN route r ON rs.route_id = r.id " +
            "WHERE r.room_id = :roomId",
            nativeQuery = true)
    Integer countStepsByRoomId(@Param("roomId") Integer roomId);
}
