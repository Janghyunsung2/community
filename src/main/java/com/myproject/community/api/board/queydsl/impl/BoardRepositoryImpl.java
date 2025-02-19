package com.myproject.community.api.board.queydsl.impl;

import com.myproject.community.api.board.BoardMainDto;
import com.myproject.community.api.board.BoardRepository;
import com.myproject.community.api.board.QBoardMainDto;
import com.myproject.community.api.board.queydsl.BoardRepositoryCustom;
import com.myproject.community.api.category.QCategoryMainDto;
import com.myproject.community.domain.board.QBoard;
import com.myproject.community.domain.category.QCategory;
import com.myproject.community.domain.category.QCategoryBoard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
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

}
