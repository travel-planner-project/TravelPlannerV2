package com.travelplanner.v2.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "비밀번호 DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordRequest {
    @Schema(description = "비밀번호", example = "123456789a")
    private String password;
}
