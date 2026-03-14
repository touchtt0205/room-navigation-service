package com.app.room_navigation_service.repository;

import com.app.room_navigation_service.entity.Step;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StepRepository extends JpaRepository<Step, Integer> {
    @Query("SELECT DISTINCT s.description FROM Step s WHERE s.description IS NOT NULL ORDER BY s.description")
    List<String> findDistinctDescriptions();
}
