package com.example.chat.chat;

import lombok.*;

@Getter
@Builder
public class ChatMessage {
    private MessageType type;
    private String content;
    private String sender; // 送信者
}
