package com.travelplanner.v2.domain.planner.plan.planner.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "플래너 수정 요청 DTO")
@Getter
@Builder
@AllArgsConstructor
public class PlannerUpdateRequest {
    @Schema(description = "플래너 인덱스", example = "1")
    private Long plannerId;

    @Schema(description = "플래너 제목", example = "제주도 여행")
    private String planTitle;

    @Schema(description = "공개여부", example = "false")
    private Boolean isPrivate;
}
