package com.seungrae.board;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // STOMP 활성화
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // /topic으로 시작하는 주소를 구독하는 클라이언트에게 메시지 전송
        registry.enableSimpleBroker("/topic");
        // 클라이언트가 서버로 메세지를 보낼 때 사용할 주소
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트가 웹소켓에 연결하기 위한 주소 설정
        // SockJs는 웹소켓을 지원하지 않는 브라우저를 위한 대체 옵션
        registry.addEndpoint("ws-stomp").withSockJS();
    }
}
