package com.myproject.community.api.category.service;

import com.myproject.community.api.category.dto.CategoryDto;
import com.myproject.community.api.category.dto.CategoryMainDto;
import java.util.List;

public interface CategoryService {
    void saveRootCategory(CategoryDto categoryDto);

    List<CategoryMainDto> getMainCategory();

    void updateCategory(long id, CategoryDto categoryDto);

    void deleteCategory(long categoryId);
}
