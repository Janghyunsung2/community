package com.myproject.community.api.comment.controller;

import com.myproject.community.api.comment.service.PostCommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/posts/comments")
@RequiredArgsConstructor
public class PostCommentAdminController {
    private final PostCommentService postCommentService;

    @GetMapping("/search")
    public ResponseEntity<?> getPostComments(@RequestParam String keyword, Pageable pageable) {
        return ResponseEntity.ok(postCommentService.getCommentByKeyword(keyword, pageable));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteByAdmin(@RequestParam("id") long id, HttpServletRequest request) {
        postCommentService.deletePostCommentAdmin(id, request);
        return ResponseEntity.noContent().build();
    }
}
