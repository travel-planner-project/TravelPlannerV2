package com.travelplanner.v2.domain.planner.chat.dto;

import lombok.Getter;

@Getter
public class ChatRequest {
    private Long userId;
    private String message;
}
