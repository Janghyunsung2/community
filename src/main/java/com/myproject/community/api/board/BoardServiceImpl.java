package com.myproject.community.api.board;

import com.myproject.community.api.category.CategoryRepository;
import com.myproject.community.domain.board.Board;
import com.myproject.community.domain.category.Category;
import com.myproject.community.domain.category.CategoryBoard;
import com.myproject.community.error.CustomException;
import com.myproject.community.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final CategoryBoardRepository categoryBoardRepository;
    private final CategoryRepository categoryRepository;


    public void createBoard(BoardWithCategoryDto boardWithCategoryDto) {

        Board board = Board.builder()
            .title(boardWithCategoryDto.getTitle())
            .description(boardWithCategoryDto.getDescription())
            .active(boardWithCategoryDto.isActive())
            .build();
        boardRepository.save(board);

        Category category = categoryRepository.findById(boardWithCategoryDto.getCategoryId())
            .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
        CategoryBoard categoryBoard = CategoryBoard.builder().board(board).category(category).build();

        categoryBoardRepository.save(categoryBoard);
    }
}
