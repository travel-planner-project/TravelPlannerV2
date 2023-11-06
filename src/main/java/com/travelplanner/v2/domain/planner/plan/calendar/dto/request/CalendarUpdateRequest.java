package com.travelplanner.v2.domain.planner.plan.calendar.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "날짜 수정 요청 DTO")
@Getter
@Builder
@AllArgsConstructor
public class CalendarUpdateRequest {
    @Schema(description = "날짜 인덱스", example = "1")
    private Long dateId;

    @Schema(description = "날짜", example = "7/14")
    private String dateTitle;
}
