package com.myproject.community.api.chat;

import java.util.List;

public interface ChatService {

    void saveMessage(ChatRequestDto chatRequestDto);
    List<ChatResponseDto> getChatMessages(long roomId);
}
