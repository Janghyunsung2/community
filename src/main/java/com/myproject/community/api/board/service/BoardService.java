package com.myproject.community.api.board.service;

import com.myproject.community.api.board.dto.BoardAdminDto;
import com.myproject.community.api.board.dto.BoardBestDto;
import com.myproject.community.api.board.dto.BoardDto;
import com.myproject.community.api.board.dto.BoardMainDto;
import com.myproject.community.api.board.dto.BoardWithCategoryDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardService {
    void createBoard(BoardWithCategoryDto boardWithCategoryDto);

    List<BoardMainDto> getBoardsByMainCategory();

    void updateBoard(long id, BoardWithCategoryDto boardWithCategoryDto);

    void deleteBoard(long id);

    Page<BoardAdminDto> getBoardsAdminPage(Pageable pageable);

    List<BoardBestDto> getBoardsBest();
}
