package com.myproject.community.api.chat;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

public interface ChatService {

    void saveMessage(long roomId, ChatMessageDto chatMessageDto);

    List<ChatMessageDto> getChatHistory(Long roomId);

    void addUserToRoom(HttpServletRequest request, long roomId,ChatMessageDto chatMessage, SimpMessageHeaderAccessor headerAccessor);
}