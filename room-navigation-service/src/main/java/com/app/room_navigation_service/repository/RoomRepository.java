package com.app.room_navigation_service.repository;

import com.app.room_navigation_service.entity.Room;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

//@Repository
//public interface RoomRepository extends JpaRepository<Room, Long> {
//    @Query("SELECT r FROM Room r " +
//            "JOIN r.building b " +
//            "JOIN b.faculty f " +
//            "WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
//            "OR LOWER(b.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
//            "OR LOWER(f.name) LIKE LOWER(CONCAT('%', :query, '%'))")
//    List<Room> searchRooms(@Param("query") String query, Pageable pageable);
//}

public interface RoomRepository extends JpaRepository<Room, Integer> {

    List<Room> findByFloorIdOrderByIdAsc(Integer floorId);

}
