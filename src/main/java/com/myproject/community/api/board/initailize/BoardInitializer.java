package com.myproject.community.api.board.initailize;

import com.myproject.community.api.board.BoardService;
import com.myproject.community.api.board.BoardWithCategoryDto;
import com.myproject.community.api.category.CategoryDto;
import com.myproject.community.api.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoardInitializer implements CommandLineRunner {

    private final BoardService boardService;
    private final CategoryService categoryService;

    @Override
    public void run(String... args) throws Exception {

        categoryService.saveRootCategory(CategoryDto.builder().name("테스트").build());

        BoardWithCategoryDto boardWithCategoryDto = BoardWithCategoryDto.builder()
            .title("테스트")
            .description("테스트")
            .active(true)
            .categoryId(1L).build();

        boardService.createBoard(boardWithCategoryDto);
    }
}
