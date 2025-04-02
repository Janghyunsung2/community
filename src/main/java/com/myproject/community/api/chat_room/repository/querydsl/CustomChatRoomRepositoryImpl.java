package com.myproject.community.api.chat_room.repository.querydsl;

import com.myproject.community.api.chat_room.ChatRoomResponseDto;
import com.myproject.community.api.chat_room.QChatRoomResponseDto;
import com.myproject.community.domain.category.QCategory;
import com.myproject.community.domain.category.QCategoryChatRoom;
import com.myproject.community.domain.chat_room.ChatRoom;
import com.myproject.community.domain.chat_room.QChatRoom;
import com.myproject.community.domain.chat_room.QChatRoomMember;
import com.myproject.community.infra.querydsl.QuerydslUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomChatRoomRepositoryImpl implements CustomChatRoomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ChatRoomResponseDto> getChatRoomsByCategoryId(long categoryId,
        Pageable pageable) {

        QChatRoom qChatRoom = QChatRoom.chatRoom;
        QCategory category = QCategory.category;
        QCategoryChatRoom categoryChatRoom = QCategoryChatRoom.categoryChatRoom;
        QChatRoomMember chatRoomMember = QChatRoomMember.chatRoomMember;

        PathBuilder<QChatRoom> entityPath = new PathBuilder<>(QChatRoom.class, "chatRoom");

        List<ChatRoomResponseDto> chatRoomResponseDtos = queryFactory.selectDistinct(
                new QChatRoomResponseDto(qChatRoom.id, qChatRoom.title, qChatRoom.capacity, chatRoomMember.id.countDistinct().intValue()))
            .from(qChatRoom)
            .join(categoryChatRoom).on(qChatRoom.id.eq(categoryChatRoom.chatRoom.id))
            .join(category).on(category.id.eq(categoryChatRoom.category.id))
            .join(chatRoomMember).on(chatRoomMember.chatRoom.id.eq(qChatRoom.id))
            .where(category.id.eq(categoryId))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(QuerydslUtils.getOrderSpecifiers(pageable, entityPath)
                .toArray(new OrderSpecifier[0]))
            .fetch();

        Long count = queryFactory.selectDistinct(qChatRoom.count())
            .from(qChatRoom)
            .join(categoryChatRoom).on(qChatRoom.id.eq(categoryChatRoom.chatRoom.id))
            .join(category).on(category.id.eq(categoryChatRoom.category.id))
            .where(category.id.eq(categoryId))
            .fetchOne();

        return new PageImpl<>(chatRoomResponseDtos, pageable, count == null ? 0 : count);
    }
}
