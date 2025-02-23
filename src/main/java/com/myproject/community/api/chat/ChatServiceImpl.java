package com.myproject.community.api.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.community.api.auth.jwt.JwtProvider;
import com.myproject.community.api.chat_room.repository.ChatRoomMemberRepository;
import com.myproject.community.api.chat_room.repository.ChatRoomRepository;
import com.myproject.community.api.member.repository.MemberRepository;
import com.myproject.community.domain.chat_room.ChatRoom;
import com.myproject.community.domain.chat_room.ChatRoomMember;
import com.myproject.community.domain.member.Member;
import com.myproject.community.error.CustomException;
import com.myproject.community.error.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String CHAT_ROOM_PREFIX = "chat_room:";
    private static final String MEMBER_PREFIX = "member:";
    private static final Duration MESSAGE_TTL = Duration.ofDays(3);

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    @Override
    public void saveMessage(long roomId, ChatRequestDto chatRequestDto, HttpServletRequest httpServletRequest) throws CustomException {
        long memberId = jwtProvider.getAuthUserId(httpServletRequest);
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        chatRequestDto.updateNickname(member.getNickName());

        try {
            String messageJson = objectMapper.writeValueAsString(chatRequestDto);
            String redisKEy = CHAT_ROOM_PREFIX + roomId;
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
