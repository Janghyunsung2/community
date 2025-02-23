package com.myproject.community.api.chat;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRequestDto {
    private String nickname;
    private String message;
    private LocalDateTime timestamp;

    public ChatRequestDto(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}
