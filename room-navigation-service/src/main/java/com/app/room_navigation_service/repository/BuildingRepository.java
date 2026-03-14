package com.app.room_navigation_service.repository;

import com.app.room_navigation_service.entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Long>{
    List<Building> findByFaculty_Id(Integer facultyId);

    @Query("SELECT COUNT(f) FROM Floor f WHERE f.building.id = :buildingId")
    Integer countTotalFloors(Integer buildingId);

}
