package com.travelplanner.v2.domain.planner.plan.calendar.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "날짜 생성 요청 DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CalendarCreateRequest {
    @Schema(description = "날짜", example = "7/14")
    // 사용자가 정한 날짜, Formatter 추가해야함.
    private String dateTitle;
}
