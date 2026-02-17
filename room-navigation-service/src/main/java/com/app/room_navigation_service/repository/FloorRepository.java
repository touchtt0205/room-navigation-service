package com.app.room_navigation_service.repository;

import com.app.room_navigation_service.DTO.FloorDTO;
import com.app.room_navigation_service.entity.Floor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FloorRepository extends JpaRepository<Floor, Integer> {

    List<Floor> findByBuildingId(Integer buildingId);
}
