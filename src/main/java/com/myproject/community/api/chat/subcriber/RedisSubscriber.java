package com.myproject.community.api.chat.subcriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.community.api.chat.ChatMessageDto;
import com.myproject.community.api.chat.ChatService;
import com.myproject.community.api.chat.ChatServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {
    private static final String CHAT_ROOM_PREFIX = "chat_room:";


    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String msgBody = new String(message.getBody());
            ChatMessageDto chatMessage = objectMapper.readValue(msgBody, ChatMessageDto.class);

            String key = CHAT_ROOM_PREFIX + chatMessage.getRoomId();


            messagingTemplate.convertAndSend("/topic/public/" + chatMessage.getRoomId(), chatMessage);

            redisTemplate.opsForList().rightPush(key, msgBody);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
