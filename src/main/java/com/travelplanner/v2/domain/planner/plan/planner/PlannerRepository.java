package com.travelplanner.v2.domain.planner.plan.planner;

import com.travelplanner.v2.domain.planner.plan.planner.domain.Planner;
import com.travelplanner.v2.domain.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlannerRepository extends JpaRepository<Planner, Long> {
    Planner findPlannerById(Long plannerId);
    List<Planner> findByUserOrderByIdDesc(User user);
    Page<Planner> findByIsPrivateFalseOrderByIdDesc(Pageable pageable);
    Page<Planner> findByIsPrivateFalseAndPlanTitleContainingOrderByIdDesc(String planTitle, Pageable pageable);
}

