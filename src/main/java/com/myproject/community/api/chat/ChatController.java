package com.myproject.community.api.chat;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;


    @MessageMapping("/chat.sendMessage/{room-id}")
    @SendTo("/topic/public/{room-id}")
    public ChatMessageDto sendMessage(@DestinationVariable("room-id") long roomId, @Payload ChatMessageDto chatMessage) {
        chatService.saveMessage(roomId, chatMessage);
        return chatMessage;
    }

    @MessageMapping("/chat.addUser/{room-id}")
    @SendTo("/topic/public/{room-id}")
    public ChatMessageDto addUser(@DestinationVariable("room-id") long roomId, @Payload ChatMessageDto chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

    @GetMapping("/api/chat/history/{room-id}")
    public ResponseEntity<List<ChatMessageDto>> getChatHistory(@PathVariable("room-id") long roomId) {
        List<ChatMessageDto> chatHistory = chatService.getChatHistory(roomId);
        return ResponseEntity.ok(chatHistory);
    }



}
