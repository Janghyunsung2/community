package com.myproject.community.api.category.service.impl;

import com.myproject.community.api.category.dto.CategoryChildrenDto;
import com.myproject.community.api.category.dto.CategoryDto;
import com.myproject.community.api.category.dto.CategoryMainDto;
import com.myproject.community.api.category.repository.CategoryRepository;
import com.myproject.community.api.category.service.CategoryService;
import com.myproject.community.domain.category.Category;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public void saveRootCategory(CategoryDto categoryDto){
        Category category = Category.builder()
            .name(categoryDto.getName()).build();
        category.updateDisplayOrder(categoryDto.getDisplayOrder());
        categoryRepository.save(category);

    }

    @Override
    public List<CategoryMainDto> getMainCategory() {
        return categoryRepository.getTop6ByOrderByOrderAsc();
    }

    @Transactional
    public void saveChildCategory(CategoryChildrenDto categoryChildrenDto){
        long parentId = categoryChildrenDto.getParent().getId();
        Category parent = categoryRepository.findById(parentId).orElse(null);

        Category child = Category.builder()
            .name(categoryChildrenDto.getName())
            .parent(parent)
            .build();

        categoryRepository.save(child);
    }



}
