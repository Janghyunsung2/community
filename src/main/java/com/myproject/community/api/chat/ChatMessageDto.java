package com.myproject.community.api.chat;

import lombok.Getter;

@Getter
public class ChatMessageDto {
    private MessageType type;
    private String content;
    private String sender;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }

    public void updateSender(String sender) {
        this.sender = sender;
    }
}
