package com.myproject.community.api.category;

import com.myproject.community.api.category.querydsl.CategoryRepositoryCustom;
import com.myproject.community.domain.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryRepositoryCustom {


}
