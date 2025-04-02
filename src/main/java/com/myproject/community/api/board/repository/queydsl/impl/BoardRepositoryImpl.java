package com.myproject.community.api.board.repository.queydsl.impl;

import com.myproject.community.api.board.dto.BoardAdminDto;
import com.myproject.community.api.board.dto.BoardBestDto;
import com.myproject.community.api.board.dto.BoardDto;
import com.myproject.community.api.board.dto.BoardMainDto;
import com.myproject.community.api.board.dto.QBoardAdminDto;
import com.myproject.community.api.board.dto.QBoardBestDto;
import com.myproject.community.api.board.dto.QBoardDto;
import com.myproject.community.api.board.dto.QBoardMainDto;
import com.myproject.community.api.board.repository.queydsl.BoardRepositoryCustom;
import com.myproject.community.api.post.dto.BoardInfoDto;
import com.myproject.community.api.post.dto.QBoardInfoDto;
import com.myproject.community.domain.board.QBoard;
import com.myproject.community.domain.category.QCategory;
import com.myproject.community.domain.category.QCategoryBoard;
import com.myproject.community.domain.post.QPost;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.Query;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    public List<BoardMainDto> getBoardMainByTopCategory(){
        QCategoryBoard qCategoryBoard = QCategoryBoard.categoryBoard;
        QBoard qBoard = QBoard.board;
        QCategory category = QCategory.category;

        return queryFactory.selectDistinct(new QBoardMainDto(qBoard.id, qBoard.title, category.id))
            .from(qBoard)
            .join(qCategoryBoard)
            .on(qCategoryBoard.board.id.eq(qBoard.id))
            .join(category)
            .on(category.id.eq(qCategoryBoard.category.id))
            .where(category.displayOrder.isNotNull().and(category.displayOrder.between(1,6).and(qBoard.active.isTrue())))
            .fetch();

    }

    @Override
    public Page<BoardAdminDto> getBoardByAdminPage(Pageable pageable) {
        QBoard qBoard = QBoard.board;
        QCategoryBoard qCategoryBoard = QCategoryBoard.categoryBoard;
        QCategory category = QCategory.category;
        // 기본 쿼리 생성
        JPAQuery<BoardAdminDto> query = queryFactory.select(
                new QBoardAdminDto(qBoard.id, qBoard.title, qBoard.description, qBoard.active, category.name))
            .from(qBoard)
            .join(qCategoryBoard).on(qBoard.id.eq(qCategoryBoard.board.id))
            .join(category).on(qCategoryBoard.category.id.eq(category.id));

        // 정렬 적용
        if (pageable.getSort().isSorted()) {
            pageable.getSort().forEach(order -> {
                PathBuilder<?> path = new PathBuilder<>(qBoard.getType(), qBoard.getMetadata());
                query.orderBy(new OrderSpecifier<>(
                    order.isAscending() ? Order.ASC : Order.DESC,
                    path.get(order.getProperty(), String.class)
                ));
            });
        }
        List<BoardAdminDto> boardDtos = query
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long count = queryFactory
            .select(qBoard.count())
            .from(qBoard)
            .fetchOne();
        return new PageImpl<>(boardDtos, pageable, count != null ? count : 0);
    }

    @Override
    public List<BoardBestDto> getBoardBests() {
        QBoard qBoard = QBoard.board;
        QPost qPost = QPost.post;
        LocalDateTime start = LocalDate.now().minusDays(7).atStartOfDay();
        LocalDateTime end = LocalDate.now().atStartOfDay();


        return queryFactory.select(new QBoardBestDto(qBoard.id, qBoard.title))
            .from(qBoard)
            .join(qPost).on(qBoard.id.eq(qPost.board.id))
            .where(qPost.createdAt.between(start, end))
            .groupBy(qBoard.id, qBoard.title)
            .orderBy(qPost.count().desc())
            .limit(5)
            .fetch();
    }

    @Override
    public Optional<BoardInfoDto> getBoardByBoardId(long boardId) {
        QBoard qBoard = QBoard.board;

       return Optional.ofNullable(queryFactory.select(new QBoardInfoDto(qBoard.id, qBoard.title))
            .from(qBoard)
            .where(qBoard.id.eq(boardId))
           .fetchOne());
    }

}
