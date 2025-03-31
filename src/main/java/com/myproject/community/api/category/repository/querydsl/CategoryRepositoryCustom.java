package com.myproject.community.api.category.repository.querydsl;

import com.myproject.community.api.category.dto.CategoryMainDto;
import com.myproject.community.api.category.dto.CategoryWithChildrenDto;
import java.util.List;

public interface CategoryRepositoryCustom {

    List<CategoryWithChildrenDto> getCategoryWithChildren();
    List<CategoryMainDto> getTopByOrderByOrderAsc();
}
