package com.myproject.community.api.chat_room.repository.querydsl;

import com.myproject.community.api.chat_room.ChatRoomResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomChatRoomRepository {

    Page<ChatRoomResponseDto> getChatRoomsByCategoryId(long categoryId, Pageable pageable);
}
