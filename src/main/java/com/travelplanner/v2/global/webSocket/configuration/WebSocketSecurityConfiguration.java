package com.travelplanner.v2.global.webSocket.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketSecurityConfiguration extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                .nullDestMatcher().permitAll()
                .simpDestMatchers("/pub/**").permitAll()
                .simpSubscribeDestMatchers("/sub/**").permitAll()
                .anyMessage().denyAll();
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true; // CSRF 보호 비활성화
    }
}
