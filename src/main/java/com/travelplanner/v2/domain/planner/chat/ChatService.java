package com.travelplanner.v2.domain.planner.chat;

import com.travelplanner.v2.domain.planner.chat.dto.ChatRequest;
import com.travelplanner.v2.domain.planner.chat.dto.ChatResponse;
import com.travelplanner.v2.domain.planner.plan.planner.domain.Planner;
import com.travelplanner.v2.domain.planner.plan.validation.ValidatingService;
import com.travelplanner.v2.domain.user.UserRepository;
import com.travelplanner.v2.domain.user.domain.User;
import com.travelplanner.v2.global.exception.ApiException;
import com.travelplanner.v2.global.exception.ErrorType;
import com.travelplanner.v2.global.webSocket.WebSocketErrorController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {
    private final UserRepository userRepository;
    private final WebSocketErrorController webSocketErrorController;
    private final ValidatingService validatingService;

    @Transactional
    public ChatResponse sendChat(ChatRequest request, Long plannerId) {
        // 유저 정보
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> {
                    webSocketErrorController.handleChatMessage(ErrorType.USER_NOT_FOUND);
                    return new ApiException(ErrorType.USER_NOT_FOUND);
                    }
                );

        // 플래너와 그룹 멤버 검증 후 플래너 리턴
        Planner planner = validatingService.validatePlannerAndUserAccess(plannerId);

        ChatResponse response = ChatResponse.builder()
                .userId(user.getId())
                .userNickname(user.getUserNickname())
                .profileImgUrl(user.getProfileImageUrl())
                .message(request.getMessage())
                .build();
        return response;
    }
}

