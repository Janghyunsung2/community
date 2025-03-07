package com.myproject.community.api.board.repository.queydsl.impl;

import com.myproject.community.api.board.dto.BoardAdminDto;
import com.myproject.community.api.board.dto.BoardDto;
import com.myproject.community.api.board.dto.BoardMainDto;
import com.myproject.community.api.board.dto.QBoardAdminDto;
import com.myproject.community.api.board.dto.QBoardDto;
import com.myproject.community.api.board.dto.QBoardMainDto;
import com.myproject.community.api.board.repository.queydsl.BoardRepositoryCustom;
import com.myproject.community.domain.board.QBoard;
import com.myproject.community.domain.category.QCategory;
import com.myproject.community.domain.category.QCategoryBoard;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.Query;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    public List<BoardMainDto> getBoardMainByTop6Category(){
        QCategoryBoard qCategoryBoard = QCategoryBoard.categoryBoard;
        QBoard qBoard = QBoard.board;
        QCategory category = QCategory.category;

        return queryFactory.selectDistinct(new QBoardMainDto(qBoard.id, qBoard.title, category.id))
            .from(qBoard)
            .join(qCategoryBoard)
            .on(qCategoryBoard.board.id.eq(qBoard.id))
            .join(category)
            .on(category.id.eq(qCategoryBoard.category.id))
            .where(category.displayOrder.isNotNull().and(category.displayOrder.between(1,6)))
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

}
