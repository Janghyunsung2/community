package com.myproject.community.api.board.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import com.myproject.community.api.board.dto.BoardAdminDto;
import com.myproject.community.api.board.dto.BoardBestDto;
import com.myproject.community.api.board.dto.BoardDto;
import com.myproject.community.api.board.dto.BoardWithCategoryDto;
import com.myproject.community.api.board.repository.BoardRepository;
import com.myproject.community.api.board.repository.CategoryBoardRepository;
import com.myproject.community.api.category.repository.CategoryRepository;
import com.myproject.community.domain.board.Board;
import com.myproject.community.domain.category.Category;
import com.myproject.community.domain.category.CategoryBoard;
import com.myproject.community.error.CustomException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class BoardServiceImplTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private CategoryBoardRepository categoryBoardRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private BoardServiceImpl boardServiceImpl;


    @Test
    @DisplayName("게시판 업데이트 성공")
    void boardUpdateSuccess(){
        long boardId = 1L;
        BoardWithCategoryDto boardWithCategoryDto = BoardWithCategoryDto.builder().title("test").active(true).description("test")
            .categoryId(1L).build();
        Board board = Board.builder().title("test").active(true).build();
        Mockito.when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));

        boardServiceImpl.updateBoard(boardId, boardWithCategoryDto);

        Mockito.verify(boardRepository, Mockito.times(1)).findById(boardId);
    }

    @Test
    @DisplayName("게시판 업데이트 중 제목 중복")
    void boardUpdateFail(){
        long boardId = 1L;
        String title = "test";
        BoardWithCategoryDto boardWithCategoryDto = BoardWithCategoryDto.builder().title(title).active(true).description("test")
            .categoryId(1L).build();
        Board board = Board.builder().title(title).active(true).build();

        Mockito.when(boardRepository.findByTitle(boardWithCategoryDto.getTitle())).thenReturn(Optional.of(board));

        Assertions.assertThrows(
            CustomException.class,() -> boardServiceImpl.updateBoard(boardId,boardWithCategoryDto));
    }

    @Test
    @DisplayName("게시판 이름 조회 성공")
    void updateBoardSuccess(){
        long boardId = 1L;
        Board board = Board.builder().title("test").active(true).build();
        Mockito.when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));

        BoardDto boardById = boardServiceImpl.getBoardById(boardId);
        Assertions.assertNotNull(boardById);
        Assertions.assertEquals("test", boardById.getTitle());
    }

    @Test
    @DisplayName("존재하지 않는 게시판")
    void updateBoardFail(){
        long boardId = 1L;
        Mockito.when(boardRepository.findById(boardId)).thenThrow(CustomException.class);
        Assertions.assertThrows(CustomException.class, () -> boardServiceImpl.getBoardById(boardId));
    }

    @Test
    @DisplayName("게시판 비활성화")
    void deleteBoardSuccess(){
        long boardId = 1L;
        Board board = Board.builder().title("test").active(true).build();
        Mockito.when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));
        boardServiceImpl.deleteBoard(boardId);
        Mockito.verify(boardRepository, Mockito.times(1)).findById(boardId);
        Assertions.assertFalse(board.isActive());
    }

    @Test
    @DisplayName("메인 카테고리 호출검증")
    void getBoardByMain(){

        boardServiceImpl.getBoardsByMainCategory();

        Mockito.verify(boardRepository, Mockito.times(1)).getBoardMainByTopCategory();
    }

    @Test
    @DisplayName("관리자 게시판페이지")
    void getBoardByCategory(){
        Pageable pageable = Pageable.ofSize(10);
        Page<BoardAdminDto> boardsAdminPage = boardServiceImpl.getBoardsAdminPage(pageable);
        Mockito.verify(boardRepository, Mockito.times(1)).getBoardByAdminPage(pageable);
    }

    @Test
    @DisplayName("추천 게시판")
    void getBoardBests(){
        BoardBestDto boardBestDto = Mockito.mock(BoardBestDto.class);
        Mockito.when(boardRepository.getBoardBests()).thenReturn(List.of(boardBestDto));
        List<BoardBestDto> boardsBest = boardServiceImpl.getBoardsBest();
        Mockito.verify(boardRepository, Mockito.times(1)).getBoardBests();
        assertEquals(boardBestDto, boardsBest.get(0));
    }

    @Test
    @DisplayName("게시판 생성")
    void createBoardSuccess(){
        BoardWithCategoryDto boardWithCategoryDto = BoardWithCategoryDto.builder().title("test").description("test").active(true).categoryId(1L).build();
        Category category = Category.builder().name("test").build();
        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        boardServiceImpl.createBoard(boardWithCategoryDto);

        Mockito.verify(boardRepository, Mockito.times(1)).save(Mockito.any(Board.class));
        Mockito.verify(categoryBoardRepository, Mockito.times(1)).save(Mockito.any(CategoryBoard.class));
    }

}