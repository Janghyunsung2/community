package com.myproject.community.api.chat;

import jakarta.servlet.http.HttpServletRequest;
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


    @PostMapping("/{room-id}/message")
    public ResponseEntity<String> saveMessage(@PathVariable long roomId, ChatRequestDto chatRequestDto, HttpServletRequest request) {
        chatService.saveMessage(roomId, chatRequestDto,request);
        return ResponseEntity.ok("Message saved to room " + roomId);
    }

    @GetMapping("/{room-id}/message")
    public ResponseEntity<String> getMessage(@PathVariable long roomId) {
        List<ChatResponseDto> chatMessages = chatService.getChatMessages(roomId);
        return ResponseEntity.ok(chatMessages.toString());
    }



}
