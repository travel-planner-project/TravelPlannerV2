package com.travelplanner.v2.domain.user.cache;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "유저 DTO")
@Getter
@Setter
public class UserDTO implements Serializable {
    @Schema(description = "유저 인덱스", example = "1")
    private String userId;

    @Schema(description = "유저 이메일", example = "test1@test.com")
    private String email;

    @Schema(description = "유저 닉네임", example = "시은")
    private String userNickname;

    @Schema(description = "provider", example = "google")
    private String provider;

    @Schema(description = "유저 프로필 이미지")
    private String profileImageUrl;
}
