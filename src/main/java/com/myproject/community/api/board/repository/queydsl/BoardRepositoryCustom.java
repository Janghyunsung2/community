package com.myproject.community.api.board.repository.queydsl;

import com.myproject.community.api.board.dto.BoardAdminDto;
import com.myproject.community.api.board.dto.BoardBestDto;
import com.myproject.community.api.board.dto.BoardDto;
import com.myproject.community.api.board.dto.BoardMainDto;
import com.myproject.community.api.post.dto.BoardInfoDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {

    List<BoardMainDto> getBoardMainByTopCategory();

    Page<BoardAdminDto> getBoardByAdminPage(Pageable pageable);

    List<BoardBestDto> getBoardBests();

    Optional<BoardInfoDto> getBoardByBoardId(long boardId);
}
