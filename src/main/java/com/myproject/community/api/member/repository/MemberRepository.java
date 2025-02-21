package com.myproject.community.api.member.repository;

import com.myproject.community.api.member.dto.MemberResponseDto;
import com.myproject.community.domain.member.Member;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface MemberRepository extends JpaRepository<Member, Long> {


    @Query("select m.id from Member m join Account a on a.member.id = m.id where a.username = :username or m.phoneNumber = :username or m.email = :username ")
    Long findByUserLoginId(String username);

    @Modifying
    @Transactional
    @Query("update Member m set m.lastLoginDate = CURRENT_DATE where m.id = :memberId")
    void updateByMemberId(@Param("memberId")long memberId);

    @Query("select case when count(m) > 0 then true else false end from Member m where m.nickName = :nickname")
    boolean existsByNickname(String nickname);

    @Query("select new com.myproject.community.api.member.dto.MemberResponseDto(m.name, a.username, m.email, m.nickName, m.phoneNumber) from Member m join Account a on a.id = m.id where m.id =:memberId")
    Optional<MemberResponseDto> findByMemberId(@Param("memberId") long memberId);
}
