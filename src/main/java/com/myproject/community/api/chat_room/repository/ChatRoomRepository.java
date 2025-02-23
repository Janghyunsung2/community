package com.myproject.community.api.chat_room.repository;

import com.myproject.community.api.chat_room.repository.querydsl.CustomChatRoomRepository;
import com.myproject.community.domain.chat_room.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>,
    CustomChatRoomRepository {

}
