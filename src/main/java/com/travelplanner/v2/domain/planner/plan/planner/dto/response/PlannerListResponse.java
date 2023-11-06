package com.travelplanner.v2.domain.planner.plan.planner.dto.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.travelplanner.v2.domain.planner.groupMember.dto.response.GroupMemberResponse;
import com.travelplanner.v2.domain.planner.plan.planner.domain.Planner;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "플래너 리스트 응답 DTO")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlannerListResponse {
    @Schema(description = "플래너 인덱스", example = "1")
    private Long plannerId;

    @Schema(description = "플래너 제목", example = "부산 여행 가기")
    private String planTitle;

    @Schema(description = "플래너 공개여부", example = "false")
    private Boolean isPrivate;

    @Schema(description = "여행 시작 날짜", example = "7/14")
    private LocalDateTime startDate;

    @Schema(description = "여행 끝 날짜", example = "7/20")
    private LocalDateTime endDate;

    private List<GroupMemberResponse> groupMembers;

    //플래너 객체
    public PlannerListResponse(Planner planner, List<GroupMemberResponse> groupMembers){
        this.plannerId = planner.getId();
        this.planTitle = planner.getPlanTitle();
        this.isPrivate = planner.getIsPrivate();
        this.startDate = planner.getStartDate();
        this.endDate = planner.getEndDate();
        this.groupMembers = groupMembers;
    }
}
