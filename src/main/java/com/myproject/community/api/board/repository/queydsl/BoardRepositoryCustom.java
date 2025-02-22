package com.myproject.community.api.board.repository.queydsl;

import com.myproject.community.api.board.dto.BoardMainDto;
import java.util.List;

public interface BoardRepositoryCustom {

    List<BoardMainDto> getBoardMainByTop6Category();
}
