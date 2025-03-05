package com.myproject.community.api.comment.repository.querydsl.impl;

import com.myproject.community.api.comment.dto.PostCommentResponseDto;
import com.myproject.community.api.comment.dto.PostCommentResponseGroupDto;
import com.myproject.community.api.comment.dto.QPostCommentResponseDto;
import com.myproject.community.api.comment.repository.querydsl.CommentRepositoryCustom;
import com.myproject.community.domain.comment.QComment;
import com.myproject.community.domain.comment.QCommentLike;
import com.myproject.community.domain.member.QMember;
import com.myproject.community.domain.post.QPost;
import com.myproject.community.domain.post.QPostComment;
import com.myproject.community.infra.querydsl.QuerydslUtils;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PostCommentResponseGroupDto> findCommentByPostId(Long postId) {
        QPost qPost = QPost.post;
        QPostComment qPostComment = QPostComment.postComment;
        QComment qComment = QComment.comment;
        QMember qMember = QMember.member;

        // 1. 루트 댓글 (부모가 null인 댓글) 조회
        List<Tuple> rootTuples = queryFactory
            .select(qComment.id, qComment.content, qComment.createdAt, qMember.nickName)
            .from(qComment)
            .join(qPostComment).on(qComment.id.eq(qPostComment.comment.id))
            .join(qPost).on(qPost.id.eq(qPostComment.post.id))
            .join(qMember).on(qMember.id.eq(qPostComment.member.id))
            .where(qPost.id.eq(postId).and(qComment.parent.isNull()))
            .fetch();

        List<PostCommentResponseDto> rootDtos = new ArrayList<>();
        List<Long> rootIds = new ArrayList<>();

        for (Tuple tuple : rootTuples) {
            Long commentId = tuple.get(qComment.id);
            String content = tuple.get(qComment.content);
            LocalDateTime createdAt = tuple.get(qComment.createdAt);
            String nickName = tuple.get(qMember.nickName);

            PostCommentResponseDto dto = new PostCommentResponseDto(commentId, content, createdAt, nickName);
            dto.setContentLikes(commentLikeCounts(commentId));
            rootDtos.add(dto);
            rootIds.add(commentId);
        }

        // 2. 자식 댓글 조회 (부모 id가 루트 댓글 id에 포함되는 댓글)
        List<Tuple> childTuples = queryFactory
            .select(qComment.id, qComment.content, qComment.createdAt, qMember.nickName, qComment.parent.id)
            .from(qComment)
            .join(qPostComment).on(qComment.id.eq(qPostComment.comment.id))
            .join(qPost).on(qPost.id.eq(qPostComment.post.id))
            .join(qMember).on(qMember.id.eq(qPostComment.member.id))
            .where(qPost.id.eq(postId).and(qComment.parent.id.in(rootIds)))
            .fetch();

        // 자식 댓글을 부모 id 기준으로 그룹화
        Map<Long, List<PostCommentResponseDto>> childrenMap = new HashMap<>();
        for (Tuple tuple : childTuples) {
            Long commentId = tuple.get(qComment.id);
            String content = tuple.get(qComment.content);
            LocalDateTime createdAt = tuple.get(qComment.createdAt);
            String nickName = tuple.get(qMember.nickName);
            Long parentId = tuple.get(qComment.parent.id);

            PostCommentResponseDto childDto = new PostCommentResponseDto(commentId, content, createdAt, nickName);
            childDto.setContentLikes((int) commentLikeCounts(commentId));
            childrenMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(childDto);
        }

        // 3. 루트 댓글과 해당 자식 댓글들을 그룹 DTO에 매핑
        List<PostCommentResponseGroupDto> result = new ArrayList<>();
        for (PostCommentResponseDto rootDto : rootDtos) {
            List<PostCommentResponseDto> childDtos = childrenMap.getOrDefault(rootDto.getContentId(), new ArrayList<>());
            result.add(new PostCommentResponseGroupDto(rootDto, childDtos));
        }

        return result;
    }

    public Page<PostCommentResponseDto> getPostCommentsByKeyword(String keyword, Pageable pageable) {
        QPost qPost = QPost.post;
        QPostComment qPostComment = QPostComment.postComment;
        QComment qComment = QComment.comment;
        QMember qMember = QMember.member;

        PathBuilder<QComment> entityPath = new PathBuilder<>(QComment.class, "comment");
        List<PostCommentResponseDto> postCommentResponseDtos = queryFactory.selectDistinct(
                new QPostCommentResponseDto(qComment.id, qComment.content, qComment.createdAt,
                    qMember.nickName))
            .from(qComment)
            .join(qPostComment).on(qComment.id.eq(qPostComment.comment.id))
            .join(qPost).on(qPost.id.eq(qPostComment.post.id))
            .join(qMember).on(qMember.id.eq(qPostComment.member.id))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(QuerydslUtils.getOrderSpecifiers(pageable, entityPath)
                .toArray(new OrderSpecifier[0]))
            .where(qComment.content.contains(keyword).or(qMember.nickName.contains(keyword)))
            .fetch();

        Long total = queryFactory
            .select(qComment.count())
            .from(qComment)
            .join(qPostComment).on(qComment.id.eq(qPostComment.comment.id))
            .fetchOne();

        return new PageImpl<>(postCommentResponseDtos, pageable, total == null ? 0 : total);
    }



    private long commentLikeCounts(long commentId){
        QCommentLike qCommentLike = QCommentLike.commentLike;
        QComment qComment = QComment.comment;
        Long count = queryFactory.selectDistinct(qComment.count())
            .from(qComment)
            .join(qCommentLike).on(qCommentLike.comment.id.eq(qComment.id))
            .where(qComment.id.eq(commentId))
            .fetchOne();
        if (!Objects.isNull(count)){
            return count;
        }
        return 0L;
    }
}
