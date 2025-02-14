package com.myproject.community.api.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.community.error.CustomException;
import com.myproject.community.error.ErrorCode;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String CHAT_ROOM_PREFIX = "chat_room:";
    private static final Duration MESSAGE_TTL = Duration.ofDays(3);

    @Override
    public void saveMessage(ChatRequestDto chatRequestDto) {
        try {
            String messageJson = objectMapper.writeValueAsString(chatRequestDto);
            String redisKEy = CHAT_ROOM_PREFIX + chatRequestDto.getRoomId();
            redisTemplate.opsForList().rightPush(redisKEy, messageJson);
            redisTemplate.expire(redisKEy, MESSAGE_TTL);
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.CHAT_BAD_REQUEST);
        }
    }

    @Override
    public List<ChatResponseDto> getChatMessages(long roomId) {
        String redisKEy = CHAT_ROOM_PREFIX + roomId;
        List<String> messageJson = redisTemplate.opsForList().range(redisKEy, 0, -1);



        return messageJson.stream().map(json -> {
            try {
                return objectMapper.readValue(json, ChatResponseDto.class);
            }catch (JsonProcessingException e){
                throw new CustomException(ErrorCode.CHAT_BAD_REQUEST);
            }
        }).toList();
    }
}
