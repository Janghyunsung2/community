package com.myproject.community.api.chat_room;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat-room")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping
    public ResponseEntity<String> createChatRoom(@RequestBody ChatRoomDto chatRoomDto, HttpServletRequest request) {
        chatRoomService.createChatRoom(chatRoomDto, request);
        return ResponseEntity.ok("Chat room created");
    }
//    @GetMapping
//    public ResponseEntity<List<ChatRoomResponseDto>> getAllChatRooms(){
//        List<ChatRoomResponseDto> allChatRooms = chatRoomService.getAllChatRooms();
//        return ResponseEntity.ok(allChatRooms);
//    }

    @GetMapping
    public ResponseEntity<ChatRoomResponseDto> getChatRoomById(@PathVariable("id") Long id){
        ChatRoomResponseDto chatRoom = chatRoomService.getChatRoom(id);
        return ResponseEntity.ok(chatRoom);
    }

    @PostMapping("{room-id}/join")
    public ResponseEntity<String> joinChatRoom(@PathVariable(name = "room-id") long roomId, HttpServletRequest request)
        throws JsonProcessingException {
        chatRoomService.joinChatRoom(roomId, request);
        return ResponseEntity.ok("Chat room joined");
    }




}
