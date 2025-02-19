package com.myproject.community.api.board.queydsl;

import com.myproject.community.api.board.BoardMainDto;
import com.myproject.community.api.board.BoardRepository;
import java.util.List;

public interface BoardRepositoryCustom {

    List<BoardMainDto> getBoardMainByTop6Category();
}
