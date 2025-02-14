package com.myproject.community.api.chat_room;

import java.util.List;

public interface ChatRoomService {

    void createChatRoom(ChatRoomDto chatRoomDto);

    ChatRoomResponseDto getChatRoom(long roomId);

    List<ChatRoomResponseDto> getAllChatRooms();
}
