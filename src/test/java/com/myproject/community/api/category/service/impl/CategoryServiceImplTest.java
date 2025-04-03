package com.myproject.community.api.category.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.myproject.community.api.category.dto.CategoryChildrenDto;
import com.myproject.community.api.category.dto.CategoryDto;
import com.myproject.community.api.category.dto.CategoryMainDto;
import com.myproject.community.api.category.dto.CategoryResponseDto;
import com.myproject.community.api.category.repository.CategoryRepository;
import com.myproject.community.domain.category.Category;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("카테고리 저장")
    void saveRootCategoryTest(){

        CategoryDto categoryDto = CategoryDto.builder().name("test").build();
        when(categoryRepository.findByName("test")).thenReturn(Optional.empty());

        categoryService.saveRootCategory(categoryDto);

        verify(categoryRepository).save(Mockito.any(Category.class));
    }

    @Test
    @DisplayName("카테고리 업데이트")
    void updateCategoryTest(){
        CategoryDto categoryDto = CategoryDto.builder().name("test").build();
        categoryDto.updateDisplayOrder(4);
        long categoryId = 1L;
        Category category = mock(Category.class);
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(category));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        categoryService.updateCategory(categoryId, categoryDto);

        verify(categoryRepository).findByName(anyString());
    }

    @Test
    @DisplayName("카테고리 삭제")
    void deleteCategoryTest(){
        categoryService.deleteCategory(1L);
        verify(categoryRepository).deleteById(1L);
    }

    @Test
    @DisplayName("전체 카테고리 조회")
    void getCategoryAllTest(){
        CategoryResponseDto categoryResponseDto = mock(CategoryResponseDto.class);
        when(categoryRepository.findAllCategories()).thenReturn(List.of(categoryResponseDto));
        List<CategoryResponseDto> categoryAll = categoryService.getCategoryAll();
        verify(categoryRepository).findAllCategories();
        assertEquals(categoryResponseDto, categoryAll.get(0));
    }

    @Test
    @DisplayName("메인 카테고리 조회")
    void getMainCategoryTest(){
        CategoryMainDto categoryMainDto = mock(CategoryMainDto.class);

        when(categoryRepository.getTopByOrderByOrderAsc()).thenReturn(List.of(categoryMainDto));
        List<CategoryMainDto> categoryMain = categoryService.getMainCategory();
        verify(categoryRepository).getTopByOrderByOrderAsc();
        assertEquals(categoryMainDto, categoryMain.get(0));
    }

    @Test
    @DisplayName("자식 카테고리 저장")
    void saveChildCategoryTest(){
        CategoryChildrenDto categoryChildrenDto = CategoryChildrenDto.builder().name("test").parent(CategoryDto.builder().name("parent").build()).build();

        when(categoryRepository.findByName(anyString())).thenReturn(Optional.empty());

        categoryService.saveChildCategory(categoryChildrenDto);

        verify(categoryRepository).save(Mockito.any(Category.class));
    }


}