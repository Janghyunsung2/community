package com.myproject.community.api.board.controller;

import com.myproject.community.api.board.dto.BoardBestDto;
import com.myproject.community.api.board.dto.BoardMainDto;
import com.myproject.community.api.board.service.BoardService;
import com.myproject.community.api.board.dto.BoardWithCategoryDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;


    @GetMapping("/api/boards/main")
    public ResponseEntity<List<BoardMainDto>> getBoardsMain(){
        return ResponseEntity.ok(boardService.getBoardsByMainCategory());
    }

    @GetMapping("/api/boards/best")
    public ResponseEntity<List<BoardBestDto>> getBoardsBest(){
        return ResponseEntity.ok(boardService.getBoardsBest());
    }

}
