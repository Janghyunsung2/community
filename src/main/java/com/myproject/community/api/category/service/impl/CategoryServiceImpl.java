package com.myproject.community.api.category.service.impl;

import com.myproject.community.api.category.dto.CategoryChildrenDto;
import com.myproject.community.api.category.dto.CategoryDto;
import com.myproject.community.api.category.dto.CategoryMainDto;
import com.myproject.community.api.category.dto.CategoryResponseDto;
import com.myproject.community.api.category.repository.CategoryRepository;
import com.myproject.community.api.category.service.CategoryService;
import com.myproject.community.domain.category.Category;
import com.myproject.community.error.CustomException;
import com.myproject.community.error.ErrorCode;
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
        boolean isDuplicate = categoryRepository.findByName(categoryDto.getName()).isPresent();
        if(isDuplicate){
            throw new CustomException(ErrorCode.CATEGORY_NAME_DUPLICATE);
        }

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
    @Override
    public void updateCategory(long id, CategoryDto categoryDto) {
        Category category = findCategoryById(id);
        String name = categoryDto.getName();
        boolean isDuplicate = categoryRepository.findByName(name).isPresent();
        if(name != null || !isDuplicate){
            category.updateName(name);
        }
        if(categoryDto.getDisplayOrder() > 0){
            category.updateDisplayOrder(categoryDto.getDisplayOrder());
        }

    }

    @Override
    public void deleteCategory(long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public List<CategoryResponseDto> getCategoryAll() {
        return categoryRepository.findAllCategories();
    }

    private Category findCategoryById(long id) {
        return categoryRepository.findById(id)
            .orElseThrow(()-> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    @Transactional
    public void saveChildCategory(CategoryChildrenDto categoryChildrenDto){
        String parentName = categoryChildrenDto.getParent().getName();
        Category parent = categoryRepository.findByName(parentName).orElse(null);

        Category child = Category.builder()
            .name(categoryChildrenDto.getName())
            .parent(parent)
            .build();

        categoryRepository.save(child);
    }



}
