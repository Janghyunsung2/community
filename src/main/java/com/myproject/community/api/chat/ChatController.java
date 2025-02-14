package com.myproject.community.api.chat;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;


    @PostMapping("/{roomId}/message")
    public ResponseEntity<String> saveMessage(@PathVariable long roomId, ChatRequestDto chatRequestDto) {
        chatService.saveMessage(chatRequestDto);
        return ResponseEntity.ok("Message saved to room " + chatRequestDto.getRoomId());
    }

    @GetMapping("/{roomId}/message")
    public ResponseEntity<String> getMessage(@PathVariable long roomId) {
        List<ChatResponseDto> chatMessages = chatService.getChatMessages(roomId);
        return ResponseEntity.ok(chatMessages.toString());
    }



}
