package com.myproject.community.api.board;

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

    @PostMapping("/api/admin/boards")
    public ResponseEntity<Void> createBoard(@RequestBody BoardWithCategoryDto boardWithCategoryDto) {
        boardService.createBoard(boardWithCategoryDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/boards/main")
    public ResponseEntity<List<BoardMainDto>> getBoardsMain(){
        return ResponseEntity.ok(boardService.getBoardsByMainCategory());
    }

}
