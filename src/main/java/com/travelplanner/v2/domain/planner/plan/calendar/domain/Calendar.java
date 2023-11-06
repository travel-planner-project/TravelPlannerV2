package com.travelplanner.v2.domain.planner.plan.calendar.domain;

import com.travelplanner.v2.domain.planner.plan.planner.domain.Planner;
import com.travelplanner.v2.domain.planner.plan.todo.domain.ToDo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Calendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dateTitle;

    @CreatedDate
    private LocalDateTime createdAt;


    // 연관 관계
    @OneToMany(mappedBy = "calendar", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @Builder.Default
    private List<ToDo> scheduleItemList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "planner_id")
    private Planner planner;


    // 연관 관계 편의 메서드
    public void mappingPlanner(Planner planner) {
        this.planner = planner;
        planner.mappingCalendar(this);
    }

    public void mappingToDo(ToDo toDo) {
        scheduleItemList.add(toDo);
    }

    public void edit(CalendarEditor calendarEditor){ // Member member
        dateTitle = calendarEditor.getDateTitle();
    }

    public CalendarEditor.CalendarEditorBuilder toEditor() {
        return CalendarEditor.builder()
                .dateTitle(dateTitle);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Calendar other = (Calendar) o;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

