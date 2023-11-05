package com.travelplanner.v2.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "닉네임 수정 DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserNicknameUpdateRequest {
    @Schema(description = "닉네임 변경", example = "지은")
    private String userNickname;
}
