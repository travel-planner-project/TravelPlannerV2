package com.travelplanner.v2.domain.planner.plan.todo;

import com.travelplanner.v2.domain.planner.plan.todo.domain.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ToDoRepository extends JpaRepository<ToDo, Long> {
    List<ToDo> findByCalendarId(Long calendarId);
}
