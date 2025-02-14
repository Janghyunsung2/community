package com.myproject.community.api.chat;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequestDto {
    private String nickname;
    private String message;
    private long userId;
    private long roomId;
    private LocalDateTime timestamp;
}
