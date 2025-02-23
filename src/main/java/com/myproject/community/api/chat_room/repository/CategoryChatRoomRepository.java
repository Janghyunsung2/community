package com.myproject.community.api.chat_room.repository;


import com.myproject.community.domain.category.CategoryChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryChatRoomRepository extends JpaRepository<CategoryChatRoom, Long> {

}
