package com.travelplanner.v2.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "프로필 상세 응답 DTO")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {

    @Schema(description = "이메일", example = "user1@gmail.com")
    private String email;

    @Schema(description = "유저 닉네임", example = "유저1")
    private String userNickname;

    @Schema(description = "프로필 이미지 url")
    private String profileImgUrl;
}
