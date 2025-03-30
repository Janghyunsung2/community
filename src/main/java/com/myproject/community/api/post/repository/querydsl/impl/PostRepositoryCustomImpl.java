package com.myproject.community.api.post.repository.querydsl.impl;

import com.myproject.community.api.post.dto.BestPostDto;
import com.myproject.community.api.post.dto.PeriodType;
import com.myproject.community.api.post.dto.PostDetailDto;
import com.myproject.community.api.post.dto.PostListDto;

import com.myproject.community.api.post.dto.PostViewRankingDto;
import com.myproject.community.api.post.dto.QBestPostDto;
import com.myproject.community.api.post.dto.QPostDetailDto;
import com.myproject.community.api.post.dto.QPostListDto;
import com.myproject.community.api.post.dto.QPostViewRankingDto;
import com.myproject.community.api.post.repository.querydsl.PostRepositoryCustom;
import com.myproject.community.domain.board.QBoard;
import com.myproject.community.domain.image.QImage;
import com.myproject.community.domain.member.QMember;
import com.myproject.community.domain.post.PostStatus;
import com.myproject.community.domain.post.QPost;
import com.myproject.community.domain.post.QPostImage;
import com.myproject.community.domain.post.QPostLike;
import com.myproject.community.infra.querydsl.QuerydslUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PostListDto> findPostsByBoardId(Long boardId, Pageable pageable) {
        QPost post = QPost.post;
        QBoard board = QBoard.board;

        PathBuilder<QPost> entityPath = new PathBuilder<>(QPost.class, "post");

        List<PostListDto> postListDtos = queryFactory
            .select(new QPostListDto(post.id, post.title, post.member.nickName, post.createdAt, post.viewCount))
            .from(post)
            .join(board)
            .on(post.board.id.eq(board.id))
            .where(post.board.id.eq(boardId).and(post.postStatus.eq(PostStatus.ACTIVE)))
            .orderBy(QuerydslUtils.getOrderSpecifiers(pageable, entityPath).toArray(new OrderSpecifier[0]))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long total = queryFactory
            .select(post.count())
            .from(post)
            .where(post.board.id.eq(boardId))
            .fetchOne();

        return new PageImpl<>(postListDtos, pageable, total == null ? 0 : total);
    }

    @Override
    public PostDetailDto findPostById(Long postId) {

        QPost post = QPost.post;
        QImage image = QImage.image;
        QPostImage postImage = QPostImage.postImage;
        QMember member = QMember.member;

        QPostLike postLike = QPostLike.postLike;


        PostDetailDto postDetailDto = queryFactory
            .select(new QPostDetailDto(post.id, post.title, post.content, member.nickName,  post.postStatus.eq(PostStatus.ACTIVE), post.viewCount))
            .from(post)
            .join(member).on(member.id.eq(post.member.id))
            .where(post.id.eq(postId))
            .fetchOne();

        Long likeCount = Optional.ofNullable(
            queryFactory
                .select(postLike.count())
                .from(postLike)
                .where(postLike.post.id.eq(postId))
                .fetchOne()
        ).orElse(0L);

        postDetailDto.setLikeCount(likeCount);

        List<String> path = queryFactory
            .select(image.path)
            .from(image)
            .join(postImage).on(postImage.image.id.eq(image.id))
            .where(postImage.post.id.eq(postId))
            .fetch();

            Objects.requireNonNull(postDetailDto).addUrlList(path);


        return postDetailDto;
    }

    @Override
    public List<PostViewRankingDto> findPostViewRankByDate(PeriodType periodType) {

        QPost post = QPost.post;
        LocalDateTime[] periodRange = getPeriodRange(periodType);

        LocalDateTime startDateTime = periodRange[0];
        LocalDateTime endDateTime = periodRange[1];

        return queryFactory.select(new QPostViewRankingDto(post.id, post.title, post.board.id))
            .from(post)
            .where(post.createdAt.between(startDateTime, endDateTime))
            .orderBy(post.viewCount.asc())
            .limit(10)
            .fetch();

    }

    @Override
    public List<BestPostDto> findBestPostByBoardId(Long boardId) {
        QPost post = QPost.post;
        QImage image = QImage.image;
        QPostImage postImage = QPostImage.postImage;

        return queryFactory.select(new QBestPostDto(post.id, post.title, image.path))
            .from(post)
            .leftJoin(postImage).on(postImage.post.id.eq(post.id))
            .leftJoin(image).on(image.id.eq(postImage.image.id))
            .where(post.board.id.eq(boardId))
            .groupBy(post.id, post.title, image.path)
            .orderBy(post.createdAt.desc())
            .limit(4)
            .fetch();
    }

    private LocalDateTime[] getPeriodRange(PeriodType periodType) {
        LocalDate today = LocalDate.now();
        return switch (periodType){
            case DAILY -> new LocalDateTime[] {
                today.atStartOfDay(),
                today.plusDays(1).atStartOfDay()
            };
            case WEEKLY -> new LocalDateTime[] {
                today.atStartOfDay(),
                today.plusWeeks(1).atStartOfDay()
            };
            case MONTHLY -> new LocalDateTime[] {
                today.atStartOfDay(),
                today.plusMonths(1).atStartOfDay()
            };
        };
    }
}
