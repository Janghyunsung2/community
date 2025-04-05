package com.myproject.community.api.chat_room;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.community.api.auth.jwt.JwtProvider;
import com.myproject.community.api.category.repository.CategoryRepository;
import com.myproject.community.api.chat.ChatMessageDto;
import com.myproject.community.api.chat.ChatMessageDto.MessageType;
import com.myproject.community.api.chat_room.repository.CategoryChatRoomRepository;
import com.myproject.community.api.chat_room.repository.ChatRoomMemberRepository;
import com.myproject.community.api.chat_room.repository.ChatRoomRepository;
import com.myproject.community.api.member.repository.MemberRepository;
import com.myproject.community.domain.category.Category;
import com.myproject.community.domain.category.CategoryChatRoom;
import com.myproject.community.domain.chat_room.ChatRoom;
import com.myproject.community.domain.chat_room.ChatRoomMember;
import com.myproject.community.domain.member.Member;
import com.myproject.community.error.CustomException;
import com.myproject.community.error.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {
    private static final String CHAT_ROOM_PREFIX = "chat_room:";

    private final ChatRoomRepository chatRoomRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryChatRoomRepository categoryChatRoomRepository;
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    @Override
    public void createChatRoom(ChatRoomDto chatRoomDto, HttpServletRequest request) {
        long memberId = jwtProvider.getAuthUserId(request);
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        ChatRoom chatRoom = ChatRoom.builder().title(chatRoomDto.getName())
            .capacity(chatRoomDto.getCapacity()).host(member).build();
        chatRoomRepository.save(chatRoom);

        long categoryId = chatRoomDto.getCategoryId();
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        categoryChatRoomRepository.save(new CategoryChatRoom(category, chatRoom));
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

    @Transactional
    @Override
    public void joinChatRoom(long roomId, HttpServletRequest request)
        throws JsonProcessingException {

        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
            .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));
        long memberId = jwtProvider.getAuthUserId(request);
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        boolean existed = chatRoomMemberRepository.existsChatRoomByChatRoomIdAndMemberId(
            chatRoom.getId(), memberId);

        int capacity = chatRoom.getCapacity();
        long memberCount = chatRoomMemberRepository.countByChatRoomId(chatRoom.getId());
        if(capacity <= memberCount) {
            throw new CustomException(ErrorCode.CHAT_BAD_REQUEST);
        }

        if(!existed) {
            ChatRoomMember chatRoomMember = new ChatRoomMember(chatRoom, member);
            chatRoomMemberRepository.save(chatRoomMember);

            ChatMessageDto chatMessage = ChatMessageDto.builder()
                .type(MessageType.JOIN)
                .roomId(roomId)
                .sender("")
                .content(member.getNickName() + "님이 입장하였습니다.")
                .build();

            String message = objectMapper.writeValueAsString(chatMessage);
            redisTemplate.convertAndSend("/topic/public/" + roomId, message);
            redisTemplate.opsForList().rightPush(CHAT_ROOM_PREFIX + roomId, message);
        }
    }

    @Override
    public Page<ChatRoomResponseDto> getChatRoomsByCategoryId(long categoryId,
        Pageable pageable) {
        return chatRoomRepository.getChatRoomsByCategoryId(
            categoryId, pageable);
    }


}
