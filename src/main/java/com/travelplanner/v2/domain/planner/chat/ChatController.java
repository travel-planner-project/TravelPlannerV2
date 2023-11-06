package com.travelplanner.v2.domain.planner.chat;

import com.travelplanner.v2.domain.planner.chat.dto.ChatRequest;
import com.travelplanner.v2.global.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    private final TokenUtil tokenUtil;

    @MessageMapping("/chat/{plannerId}")
    public void sendChat(@DestinationVariable Long plannerId, ChatRequest request, @Header("Authorization") String accessToken) {
        tokenUtil.getAuthenticationFromToken(accessToken);
        messagingTemplate.convertAndSend("/sub/planner-message/" + plannerId,
                Map.of("type","chat", "msg", chatService.sendChat(request, plannerId)
                )
        );
    }
}

