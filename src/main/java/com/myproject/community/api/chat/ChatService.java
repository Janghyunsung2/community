package com.myproject.community.api.chat;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

public interface ChatService {

    void saveMessage(long roomId, ChatMessageDto chatMessageDto);

    List<ChatMessageDto> getChatHistory(Long roomId);
}