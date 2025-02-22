package com.myproject.community.api.board.repository;

import com.myproject.community.api.board.repository.queydsl.BoardRepositoryCustom;
import com.myproject.community.domain.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {

}
