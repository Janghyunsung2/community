package com.myproject.community.api.member.repository.querydsl;

import com.myproject.community.api.member.dto.MemberAdminResponseDto;
import com.myproject.community.api.member.dto.QMemberAdminResponseDto;
import com.myproject.community.domain.account.QAccount;
import com.myproject.community.domain.gender.QGender;
import com.myproject.community.domain.member.QMember;
import com.myproject.community.domain.member.QMemberStatus;
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
public class CustomMemberRepositoryImpl implements CustomMemberRepository {

    private final JPAQueryFactory queryFactory;


    public Page<MemberAdminResponseDto> getAdminPageMemberList(Pageable pageable) {

        QMember qMember = QMember.member;
        QMemberStatus qMemberStatus = QMemberStatus.memberStatus;
        QGender qGender = QGender.gender;
        QAccount qAccount = QAccount.account;

        PathBuilder<QMember> entityPath = new PathBuilder<>(QMember.class, "member");

        List<MemberAdminResponseDto> memberAdminResponseDtos = queryFactory.select(
                new QMemberAdminResponseDto(qMember.id, qMember.name, qMember.nickName, qMember.email,
                    qMember.phoneNumber, qMemberStatus.status.stringValue(), qGender.name, qAccount.role.stringValue()))
            .from(qMember)
            .leftJoin(qMemberStatus).on(qMember.memberStatus.id.eq(qMemberStatus.id)).fetchJoin()
            .leftJoin(qGender).on(qGender.id.eq(qMember.gender.id)).fetchJoin()
            .leftJoin(qAccount).on(qAccount.member.id.eq(qMember.id)).fetchJoin()
            .orderBy(QuerydslUtils.getOrderSpecifiers(pageable, entityPath)
                .toArray(new OrderSpecifier[0]))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long count = queryFactory.selectDistinct(qMember.count())
            .from(qMember)
            .fetchOne();

        return new PageImpl<>(memberAdminResponseDtos, pageable, count != null ? count : 0L);
    }
}
