package com.myproject.community.api.board.repository.queydsl;

import com.myproject.community.api.board.dto.BoardAdminDto;
import com.myproject.community.api.board.dto.BoardBestDto;
import com.myproject.community.api.board.dto.BoardDto;
import com.myproject.community.api.board.dto.BoardMainDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {

    List<BoardMainDto> getBoardMainByTop6Category();

    Page<BoardAdminDto> getBoardByAdminPage(Pageable pageable);

    List<BoardBestDto> getBoardBests();
}
