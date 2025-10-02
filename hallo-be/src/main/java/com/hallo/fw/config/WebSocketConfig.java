package com.hallo.fw.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 注册 STOMP 端点，客户端将使用这个端点连接到 WebSocket 服务器
        registry.addEndpoint("/ws")
                // .setAllowedOrigins("*") // 允许所有来源的跨域请求
                .setAllowedOriginPatterns("*")
                .withSockJS(); // 启用 SockJS 回退选项
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 客户端→服务器：客户端消息以 /server/send-message 开头的地址将被转发到
        // @MessageMapping("/send-message")
        registry.setApplicationDestinationPrefixes("/server");

        // 服务器→客户端：
        // 消息以 /topic 开头的地址将被转发到 @SendTo("/topic/{destination}")
        // 消息以 /user 开头的地址将被转发到 @SendToUser("/user/{destination}")
        registry.enableSimpleBroker("/topic", "/user");

        // 服务器通过 @SendToUser("/private") 发送消息 → 实际目的地为 /user/{userId}/private ，其中
        // {userId} 是动态替换的用户标识。
        registry.setUserDestinationPrefix("/user");
    }
}