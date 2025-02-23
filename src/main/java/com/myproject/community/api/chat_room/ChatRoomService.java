package com.myproject.community.api.chat_room;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatRoomService {

    void createChatRoom(ChatRoomDto chatRoomDto, HttpServletRequest request);

    ChatRoomResponseDto getChatRoom(long roomId);

    void joinChatRoom(long roomId, HttpServletRequest request);

    Page<ChatRoomResponseDto> getChatRoomsByCategoryId(long categoryId, Pageable pageable);
}
