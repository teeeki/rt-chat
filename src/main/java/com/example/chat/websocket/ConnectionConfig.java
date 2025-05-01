package com.example.chat.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration // 設定クラスのアノテーション
@EnableWebSocketMessageBroker // WebSocketとStompを有効にするアノテーション
public class ConnectionConfig implements WebSocketMessageBrokerConfigurer {

    // エンドポイントの設定
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS(); // 接続先のエンドポイント
    }

    // メッセージブローカーの設定
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app"); // 送信先
        registry.enableSimpleBroker("/topic"); // 送受信を仲介
    }
}
