package com.myproject.community.api.category.repository;

import com.myproject.community.api.category.dto.CategoryResponseDto;
import com.myproject.community.api.category.repository.querydsl.CategoryRepositoryCustom;
import com.myproject.community.domain.category.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryRepositoryCustom {

    @Query("select c from Category c where c.name =:name")
    Optional<Category> findByName(String name);

    @Query("select new com.myproject.community.api.category.dto.CategoryResponseDto(c.id, c.name) from Category c")
    List<CategoryResponseDto> findAllCategories();
}
