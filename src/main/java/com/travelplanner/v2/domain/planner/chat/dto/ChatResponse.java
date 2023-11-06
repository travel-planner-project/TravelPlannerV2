package com.travelplanner.v2.domain.planner.chat.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class ChatResponse {
    private Long id;
    private Long userId;
    private String userNickname;
    private String profileImgUrl;
    private String message;
}

