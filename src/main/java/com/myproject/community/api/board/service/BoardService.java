package com.myproject.community.api.board.service;

import com.myproject.community.api.board.dto.BoardMainDto;
import com.myproject.community.api.board.dto.BoardWithCategoryDto;
import java.util.List;

public interface BoardService {
    void createBoard(BoardWithCategoryDto boardWithCategoryDto);

    List<BoardMainDto> getBoardsByMainCategory();
}
