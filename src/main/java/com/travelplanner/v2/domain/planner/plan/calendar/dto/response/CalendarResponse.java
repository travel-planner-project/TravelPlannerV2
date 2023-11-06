package com.travelplanner.v2.domain.planner.plan.calendar.dto.response;

import com.travelplanner.v2.domain.planner.plan.todo.dto.ToDoResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalendarResponse {

    private Long dateId;
    private String dateTitle;
    @CreatedDate
    private LocalDateTime createAt;
    private Long plannerId;
    private List<ToDoResponse> scheduleItemList;

    public static class CalendarResponseBuilder {
        private List<ToDoResponse> scheduleItemList;
        public CalendarResponseBuilder scheduleItemList(List<ToDoResponse> scheduleItemList) {
            this.scheduleItemList = scheduleItemList;
            return this;
        }
    }
}

