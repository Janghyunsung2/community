package com.myproject.community.api.board.repository;

import com.myproject.community.domain.category.CategoryBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryBoardRepository extends JpaRepository<CategoryBoard, Long> {

}
