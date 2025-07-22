package com.hallo.config;

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
        // 配置消息代理
        // 客户端发送消息的目的地前缀
        registry.setApplicationDestinationPrefixes("/chat");

        // 服务器向客户端推送消息的目的地前缀
        registry.enableSimpleBroker("/topic", "/user");

        // 设置用户目的地前缀
        registry.setUserDestinationPrefix("/user");
    }
}