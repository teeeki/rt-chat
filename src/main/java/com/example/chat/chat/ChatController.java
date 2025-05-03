package com.example.chat.chat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

/**
 * WebSocketを介したチャットメッセージの送受信を処理するコントローラークラス
 * STOMPプロトコルを使用し、クライアントから送られてきたメッセージを全ユーザーに送信する
 */
@Controller
public class ChatController {

    /**
     * クライアントから送信されたチャットメッセージを受信し、全クライアントにブロードキャストする。
     * 
     * {@code /chat.sendMessage} エンドポイントから受信したメッセージを
     * {@code /topic/public} 宛てに送信する。
     *
     * @param chatMessage クライアントから送信されたチャットメッセージ（JSON → {@link ChatMessage}
     *                    にマッピングされる）
     * @return 全クライアントに送信されるチャットメッセージ
     */
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    /**
     * チャットに新しいユーザーが参加した際に呼び出され、セッションにユーザー名を登録し、
     * 全クライアントに通知メッセージを送信する
     *
     * {@code /chat.addUser} エンドポイントからのリクエストを受け取り、ユーザー名を
     * WebSocket セッション属性に追加する。その後、通知メッセージを
     * {@code /topic/public} に送信することで、全クライアントに新しいユーザーの参加を知らせる。
     *
     * @param chatMessage    ユーザー情報を含むメッセージ（JSON → {@link ChatMessage} にマッピングされる）
     * @param headerAccessor セッション情報にアクセスするための STOMP ヘッダーアクセサ
     * @return 全クライアントに送信される参加通知用のチャットメッセージ
     */
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(
            @Payload ChatMessage chatMessage,
            StompHeaderAccessor headerAccessor) {
        // セッションにユーザ名を登録
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
}
