package com.myproject.community.api.board;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping("")
    public ResponseEntity<Void> createBoard(@RequestBody BoardWithCategoryDto boardWithCategoryDto) {
        boardService.createBoard(boardWithCategoryDto);
        return ResponseEntity.ok().build();
    }

}
