package com.app.room_navigation_service.repository;

import com.app.room_navigation_service.DTO.FloorDTO;
import com.app.room_navigation_service.entity.Floor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FloorRepository extends JpaRepository<Floor, Integer> {

    List<Floor> findByBuildingId(Integer buildingId);

    @Query("SELECT COUNT(r) FROM Room r WHERE r.floor.id = :floorId")
    Integer countTotalRooms(Integer floorId);
}
