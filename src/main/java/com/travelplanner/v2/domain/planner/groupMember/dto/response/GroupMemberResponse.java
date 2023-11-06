package com.travelplanner.v2.domain.planner.groupMember.dto.response;

import com.travelplanner.v2.domain.planner.groupMember.domain.GroupMemberType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "그룹멤버 생성 응답 DTO")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupMemberResponse {
    @Schema(description = "그룹멤버 인덱스", example = "1")
    private Long groupMemberId;

    @Schema(description = "유저 이메일" , example = "test@naver.com")
    private String email;

    @Schema(description = "유저 닉네임", example = "시니")
    private String nickname;

    @Schema(description = "유저 인덱스", example = "1")
    private Long userId;

    @Schema(description = "프로필 이미지 주소")
    private String profileImageUrl;

    @Schema(description = "그룹 역할", example = "HOST")
    private GroupMemberType role;

}