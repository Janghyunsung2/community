package com.myproject.community.api.category.repository;

import com.myproject.community.api.category.repository.querydsl.CategoryRepositoryCustom;
import com.myproject.community.domain.category.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryRepositoryCustom {

    @Query("select c from Category c where c.name =:name")
    Optional<Category> findByName(String name);
}
