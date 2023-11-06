package com.travelplanner.v2.domain.auth.mail.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdatePasswordDto {
    private String newPassword;
}