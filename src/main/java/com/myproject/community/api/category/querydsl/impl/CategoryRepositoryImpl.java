package com.myproject.community.api.category.querydsl.impl;

import com.myproject.community.api.category.CategoryWithChildrenDto;
import com.myproject.community.api.category.CategoryDto;
import com.myproject.community.api.category.QCategoryDto;
import com.myproject.community.api.category.querydsl.CategoryRepositoryCustom;
import com.myproject.community.domain.category.QCategory;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    // 모든 최상위 카테고리와 자식카테고리 조회
    public List<CategoryWithChildrenDto> getCategoryWithChildren() {

        QCategory category = QCategory.category;
        List<CategoryDto> parentCategories = queryFactory
            .select(new QCategoryDto(category.id, category.name))
            .from(category)
            .where(category.parent.isNull())
            .fetch();

        return parentCategories.stream()
            .map(parentDto -> {
                List<CategoryDto> children = queryFactory
                    .select(new QCategoryDto(category.id, category.name))
                    .from(category)
                    .where(category.parent.id.eq(parentDto.getId()))
                    .fetch();

                return new CategoryWithChildrenDto(parentDto, children);
            })
            .toList();
    }


}
