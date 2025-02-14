package com.myproject.community.api.chat_room;

import com.myproject.community.domain.chat_room.ChatRoom;
import com.myproject.community.error.CustomException;
import com.myproject.community.error.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    @Override
    public void createChatRoom(ChatRoomDto chatRoomDto) {
        ChatRoom chatRoom = ChatRoom.builder().title(chatRoomDto.getName())
            .capacity(chatRoomDto.getCapacity()).build();
        chatRoomRepository.save(chatRoom);
    }

    @Transactional(readOnly = true)
    @Override
    public ChatRoomResponseDto getChatRoom(long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
            .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));
        return ChatRoomResponseDto.builder()
            .id(chatRoom.getId())
            .title(chatRoom.getTitle())
            .capacity(chatRoom.getCapacity())
            .build();
    }

    @Override
    public List<ChatRoomResponseDto> getAllChatRooms() {
        return chatRoomRepository.findAll().stream().map(chatRoom ->
            ChatRoomResponseDto.builder()
                .id(chatRoom.getId())
                .title(chatRoom.getTitle())
                .capacity(chatRoom.getCapacity())
                .build()
        ).toList();
    }
}
