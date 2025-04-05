package com.myproject.community.api.chat_room.repository;

import com.myproject.community.domain.chat_room.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {


    @Query("SELECT CASE WHEN COUNT(cm) > 0 THEN true ELSE false END " +
        "FROM ChatRoomMember cm " +
        "WHERE cm.chatRoom.id = :chatRoomId AND cm.member.id = :memberId")
    boolean existsChatRoomByChatRoomIdAndMemberId(@Param("chatRoomId") Long chatRoomId,
        @Param("memberId") Long memberId);

    @Modifying
    @Query("delete from ChatRoomMember crm where crm.member.id =:memberId")
    void deleteByMemberId(Long memberId);

    @Query("select count(crm.id) from ChatRoomMember crm where crm.chatRoom.id =:chatRoomId")
    long countByChatRoomId(Long chatRoomId);
}
