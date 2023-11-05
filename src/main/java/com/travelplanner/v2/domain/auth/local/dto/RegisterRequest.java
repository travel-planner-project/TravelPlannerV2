package com.travelplanner.v2.domain.auth.local.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "회원가입 요청 DTO")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @Schema(description = "닉네임", example = "유저1")
    private String userNickname;

    @Schema(description = "이메일", example = "user1@gmail.com")
    private String email;

    @Schema(description = "비밀번호", example = "123456879")
    private String password;
}
