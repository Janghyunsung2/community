package com.myproject.community.api.category.initailize;

import com.myproject.community.api.board.BoardService;
import com.myproject.community.api.board.BoardWithCategoryDto;
import com.myproject.community.api.category.CategoryDto;
import com.myproject.community.api.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryInitialize implements CommandLineRunner {

    private final CategoryService categoryService;
    private final BoardService boardService;
    private final String CATEGORY_NAME = "임시_";
    private final String BOARD_NAME = "게시판_";

    @Override
    public void run(String... args) throws Exception {
        for (int i = 1; i <= 10; i++) {
            CategoryDto categoryDto = CategoryDto.builder().name(CATEGORY_NAME + i).build();
            categoryDto.updateDisplayOrder(i);
            categoryService.saveRootCategory(categoryDto);
            BoardWithCategoryDto boardWithCategoryDto = BoardWithCategoryDto.builder()
                .title(BOARD_NAME + i)
                .description("테스트")
                .active(true)
                .categoryId(i).build();
            boardService.createBoard(boardWithCategoryDto);
        }
    }
}
