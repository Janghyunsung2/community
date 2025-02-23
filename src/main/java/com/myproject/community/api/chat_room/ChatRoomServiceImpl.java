package com.myproject.community.api.chat_room;

import com.myproject.community.api.auth.jwt.JwtProvider;
import com.myproject.community.api.category.repository.CategoryRepository;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryChatRoomRepository categoryChatRoomRepository;
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    @Transactional
    @Override
    public void createChatRoom(ChatRoomDto chatRoomDto, HttpServletRequest request) {
        long memberId = jwtProvider.getAuthUserId(request);
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        ChatRoom chatRoom = ChatRoom.builder().title(chatRoomDto.getName())
            .capacity(chatRoomDto.getCapacity()).member(member).build();
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

    @Override
    public void joinChatRoom(long roomId, HttpServletRequest request) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
            .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));
        long memberId = jwtProvider.getAuthUserId(request);
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        chatRoomMemberRepository.save(new ChatRoomMember(chatRoom, member));
    }

    @Override
    public Page<ChatRoomResponseDto> getChatRoomsByCategoryId(long categoryId,
        Pageable pageable) {
        return chatRoomRepository.getChatRoomsByCategoryId(categoryId, pageable);
    }


}
