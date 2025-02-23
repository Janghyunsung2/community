package com.myproject.community.api.chat_room;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/category/{id}/chat-room")
@RequiredArgsConstructor
public class ChatRoomCategoryController {

    private final ChatRoomService chatRoomService;

    @GetMapping
    public ResponseEntity<?> getChatRoomCategory(@PathVariable("id") long id, Pageable pageable) {
        return ResponseEntity.ok(chatRoomService.getChatRoomsByCategoryId(id, pageable));
    }
}
