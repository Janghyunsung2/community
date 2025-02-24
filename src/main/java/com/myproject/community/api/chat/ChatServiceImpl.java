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
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String CHAT_ROOM_PREFIX = "chat_room:";
    private static final String MEMBER_PREFIX = "member:";
    private static final Duration MESSAGE_TTL = Duration.ofDays(3);

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    @Transactional(readOnly = true)
    @Override
    public void saveMessage(long roomId, ChatMessageDto chatMessageDto) throws CustomException {
//        long memberId = jwtProvider.getAuthUserId(httpServletRequest);
//        Member member = memberRepository.findById(memberId)
//            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
//
//        chatMessageDto.updateSender(member.getNickName());
        String key = CHAT_ROOM_PREFIX + roomId;

        try {
           String jsonMessage = objectMapper.writeValueAsString(chatMessageDto);
            redisTemplate.opsForList().rightPush(key, jsonMessage);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public List<ChatMessageDto> getChatHistory(Long roomId) {
        String key = CHAT_ROOM_PREFIX + roomId;
        List<Object> messages = redisTemplate.opsForList().range(key, 0, -1);

        return messages.stream()
            .map(obj -> {
                try {
                    // ✅ JSON 문자열을 ChatMessageDto로 변환
                    return objectMapper.readValue(obj.toString(), ChatMessageDto.class);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    return null; // 변환 실패한 경우 null 반환
                }
            })
            .filter(msg -> msg != null) // 변환 실패한 메시지는 필터링
            .collect(Collectors.toList());
    }





}
