package com.myproject.community.api.board.service.impl;

import com.myproject.community.api.board.repository.BoardRepository;
import com.myproject.community.api.board.repository.CategoryBoardRepository;
import com.myproject.community.api.board.dto.BoardDto;
import com.myproject.community.api.board.dto.BoardMainDto;
import com.myproject.community.api.board.dto.BoardWithCategoryDto;
import com.myproject.community.api.board.service.BoardService;
import com.myproject.community.api.category.repository.CategoryRepository;
import com.myproject.community.domain.board.Board;
import com.myproject.community.domain.category.Category;
import com.myproject.community.domain.category.CategoryBoard;
import com.myproject.community.error.CustomException;
import com.myproject.community.error.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final CategoryBoardRepository categoryBoardRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public void createBoard(BoardWithCategoryDto boardWithCategoryDto) {
        if (Boolean.TRUE.equals(isDuplicateBoardTitle(boardWithCategoryDto.getTitle()))) {
            return;
        }
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
    public void updateBoard(long id, BoardWithCategoryDto boardWithCategoryDto) {

        if (isDuplicateBoardTitle(boardWithCategoryDto.getTitle())) {
            throw new CustomException(ErrorCode.BOARD_TITLE_DUPLICATE);
        }
        Board board = findBoardById(id);
        board.update(boardWithCategoryDto.getTitle(), boardWithCategoryDto.getDescription(), boardWithCategoryDto.isActive());
    }

    public BoardDto getBoardById(Long id) {
        Board board = boardRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        return new BoardDto(board.getId(), board.getTitle(), board.getDescription(), board.isActive());
    }

    @Transactional(readOnly = true)
    public Page<BoardDto> getBoardsAdminPage(Pageable pageable) {
        return boardRepository.getBoardByAdminPage(pageable);
    }

    @Transactional
    public void deleteBoard(long id) {
        findBoardById(id).unActivate();
    }

    private Boolean isDuplicateBoardTitle(String title) {
        return boardRepository.findByTitle(title).isPresent();
    }

    private Board findBoardById(long id) {
        return boardRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
    }


}
