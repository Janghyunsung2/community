package com.myproject.community.api.chat;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

public interface ChatService {

    void saveMessage(long roomId, ChatRequestDto chatRequestDto, HttpServletRequest request);
    List<ChatResponseDto> getChatMessages(long roomId);
}
