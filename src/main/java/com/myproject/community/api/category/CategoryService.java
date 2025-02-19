package com.myproject.community.api.category;

import java.util.List;

public interface CategoryService {
    void saveRootCategory(CategoryDto categoryDto);

    List<CategoryMainDto> getMainCategory();
}
