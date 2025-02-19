package com.myproject.community.api.category.querydsl;

import com.myproject.community.api.category.CategoryDto;
import com.myproject.community.api.category.CategoryMainDto;
import com.myproject.community.api.category.CategoryWithChildrenDto;
import java.util.List;

public interface CategoryRepositoryCustom {

    List<CategoryWithChildrenDto> getCategoryWithChildren();
    List<CategoryMainDto> getTop6ByOrderByOrderAsc();
}
