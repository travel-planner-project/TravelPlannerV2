package com.travelplanner.v2.domain.planner.groupMember.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "그룹멤버 추가 요청 DTO")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupMemberDeleteRequest {
    @Schema(description = "그룹멤버 인덱스")
    private Long groupMemberId;
}
