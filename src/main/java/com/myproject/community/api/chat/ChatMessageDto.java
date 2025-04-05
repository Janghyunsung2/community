package com.myproject.community.api.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ChatMessageDto {
    private MessageType type;
    private String content;
    private String sender;
    private Long roomId;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }

    public void updateSender(String sender) {
        this.sender = sender;
    }
}
