package com.myproject.community.api.board.repository;

import com.myproject.community.api.board.repository.queydsl.BoardRepositoryCustom;
import com.myproject.community.domain.board.Board;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {

    @Query("select b from Board b where b.title =:title")
    Optional<Board> findByTitle(String title);
}
