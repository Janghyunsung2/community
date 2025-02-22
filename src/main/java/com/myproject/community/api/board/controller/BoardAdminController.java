package com.myproject.community.api.board.controller;

import com.myproject.community.api.board.dto.BoardWithCategoryDto;
import com.myproject.community.api.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/boards")
@RequiredArgsConstructor
public class BoardAdminController {

    private final BoardService boardService;


    @PostMapping
    public ResponseEntity<?> createBoards(@RequestBody BoardWithCategoryDto boardWithCategoryDto) {
        boardService.createBoard(boardWithCategoryDto);
        return ResponseEntity.ok().build();
    }
    @PutMapping
    public ResponseEntity<?> updateBoards(@RequestParam long id, @RequestBody BoardWithCategoryDto boardWithCategoryDto) {
        boardService.updateBoard(id, boardWithCategoryDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteBoards(@RequestParam long id) {
        boardService.deleteBoard(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> getAdminPageBoards(Pageable pageable) {
        return ResponseEntity.ok(boardService.getBoardsAdminPage(pageable));
    }

}
