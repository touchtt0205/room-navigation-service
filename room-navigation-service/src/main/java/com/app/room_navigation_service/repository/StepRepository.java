package com.app.room_navigation_service.repository;

import com.app.room_navigation_service.entity.Step;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StepRepository extends JpaRepository<Step, Integer> {
}
