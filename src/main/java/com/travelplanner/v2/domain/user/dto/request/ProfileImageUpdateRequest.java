package com.travelplanner.v2.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "프로필 수정 DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileImageUpdateRequest {
    @Schema(description = "이미지 변경 여부", example = "false")
    private Boolean changeProfileImage;
}
