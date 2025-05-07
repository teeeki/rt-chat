package com.example.chat.websocket;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.example.chat.chat.ChatMessage;

import lombok.RequiredArgsConstructor;

import com.example.chat.chat.MessageType;

/**
 * WebSocketの切断収量イベントを処理する
 * ユーザの切断を検知してチャットにその情報を通知する
 */
@Component
@RequiredArgsConstructor
public class ConnectionEventListener {

    private final SimpMessageSendingOperations messageTemplate;

    /**
     * websocketのセッションが切断されると@EventListenerアノテーションによって
     * 呼び出される
     * 
     * @param event 切断イベント情報を含む {@link SessionDisconnectEvent}
     */
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        // ユーザ名取得
        String username = (String) headerAccessor.getSessionAttributes().get("username");

        if (username != null) {
            // 退出メッセージ策差異
            ChatMessage chatMessage = ChatMessage.builder()
                    .type(MessageType.LEAVE)
                    .sender(username)
                    .build();

            // 全ユーザに退出メッセージを送信
            messageTemplate.convertAndSend("/topic/public", chatMessage);
        }

    }
}
