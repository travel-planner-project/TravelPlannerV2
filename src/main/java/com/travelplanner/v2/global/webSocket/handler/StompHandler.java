package com.travelplanner.v2.global.webSocket.handler;

import com.travelplanner.v2.global.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class StompHandler implements ChannelInterceptor {
    private final TokenUtil tokenUtil;
    // websocket을 통해 들어온 요청이 처리되기 전 실행됨

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String accessToken = accessor.getFirstNativeHeader("Authorization");
        log.info("====================================================");
        log.info("Received STOMP Message: " + message);
        log.info("All headers: " + accessor.toNativeHeaderMap());
        log.info("Access Token: " + accessToken);
        log.info("Incoming message type: " + accessor.getMessageType());
        log.info("====================================================");

        // websocket 연결 시 헤더의 JWT 토큰 유효성 검증
        if (SimpMessageType.CONNECT.equals(accessor.getMessageType())
                || SimpMessageType.MESSAGE.equals(accessor.getMessageType())) {
            log.info("====================================================");
            log.info("accessor: " + accessor.getMessageType());
            log.info("====================================================");

            if (tokenUtil.isValidToken(accessToken)) {
                tokenUtil.getAuthenticationFromToken(accessToken);
            }
        }
        return message;
    }
}
