package com.myproject.community.api.chat_room;

import com.myproject.community.domain.chat_room.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

}
