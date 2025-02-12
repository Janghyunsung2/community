package com.myproject.community.api.category;

import com.myproject.community.domain.category.Category;
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
        categoryRepository.save(category);

    }

    @Transactional
    public void saveChildCategory(CategoryChildrenDto categoryChildrenDto){
        CategoryDto parent = categoryChildrenDto.getParent();
        Category child = Category.builder()
            .name(categoryChildrenDto.getName())
            .parent(Category.builder()
                .id(parent.getId())
                .name(parent.getName())
                .build()
            ).build();

        categoryRepository.save(child);
    }



}
