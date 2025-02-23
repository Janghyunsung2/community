package com.myproject.community.api.chat_room.repository;

import com.myproject.community.domain.chat_room.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {

}
