package com.myproject.community.api.board;

import java.util.List;

public interface BoardService {
    void createBoard(BoardWithCategoryDto boardWithCategoryDto);

    List<BoardMainDto> getBoardsByMainCategory();
}
