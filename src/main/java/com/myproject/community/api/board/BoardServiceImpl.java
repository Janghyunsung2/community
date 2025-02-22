package com.myproject.community.api.board;

import com.myproject.community.api.category.repository.CategoryRepository;
import com.myproject.community.domain.board.Board;
import com.myproject.community.domain.category.Category;
import com.myproject.community.domain.category.CategoryBoard;
import com.myproject.community.error.CustomException;
import com.myproject.community.error.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public List<BoardMainDto> getBoardsByMainCategory() {
        return boardRepository.getBoardMainByTop6Category();
    }

    @Transactional
    public void updateBoard(BoardWithCategoryDto boardWithCategoryDto) {
        Board board = boardRepository.findById(boardWithCategoryDto.getId())
            .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        board.update(boardWithCategoryDto.getTitle(), boardWithCategoryDto.getDescription(), boardWithCategoryDto.isActive());
    }

    public BoardDto getBoardById(Long id) {
        Board board = boardRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        return new BoardDto(board.getId(), board.getTitle(), board.getDescription(), board.isActive());
    }
}
