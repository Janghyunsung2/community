package com.myproject.community.api.category.repository.querydsl.impl;

import com.myproject.community.api.category.dto.CategoryMainDto;
import com.myproject.community.api.category.dto.CategoryWithChildrenDto;
import com.myproject.community.api.category.dto.CategoryDto;

import com.myproject.community.api.category.dto.QCategoryDto;
import com.myproject.community.api.category.dto.QCategoryMainDto;
import com.myproject.community.api.category.repository.querydsl.CategoryRepositoryCustom;
import com.myproject.community.domain.category.QCategory;
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
            .select(new QCategoryDto(category.name))
            .from(category)
            .where(category.parent.isNull())
            .fetch();

        return parentCategories.stream()
            .map(parentDto -> {
                List<CategoryDto> children = queryFactory
                    .select(new QCategoryDto(category.name))
                    .from(category)
                    .where(category.parent.name.eq(parentDto.getName()))
                    .fetch();

                return new CategoryWithChildrenDto(parentDto, children);
            })
            .toList();
    }

    public List<CategoryMainDto> getTop6ByOrderByOrderAsc() {
        QCategory category = QCategory.category;
        return queryFactory.select(new QCategoryMainDto(category.id, category.name, category.displayOrder))
            .from(category)
            .where(category.displayOrder.between(1, 6))
            .orderBy(category.displayOrder.asc())
            .fetch();
    }


}
