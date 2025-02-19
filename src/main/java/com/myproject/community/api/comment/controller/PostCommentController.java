package com.myproject.community.api.comment.controller;

import com.myproject.community.api.comment.PostCommentRequestDto;
import com.myproject.community.api.comment.PostCommentResponseGroupDto;
import com.myproject.community.api.comment.service.PostCommentService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostCommentController {

    private final PostCommentService postCommentService;

    @GetMapping("/api/posts/{postId}/comments")
    public ResponseEntity<List<PostCommentResponseGroupDto>> getCommentByPostId(@PathVariable Long postId) {
        return ResponseEntity.ok(postCommentService.getCommentGroupByPostId(postId));
    }

    @PostMapping("/api/posts/{postId}/comments")
    public ResponseEntity<Void> createCommentByPostId(@PathVariable Long postId,@Validated @RequestBody PostCommentRequestDto postCommentRequestDto, HttpServletRequest request) {
        postCommentService.postCommentSave(postId, postCommentRequestDto, request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/api/comments/{commentId}")
    public ResponseEntity<Void> updateComment(@PathVariable long commentId, @RequestBody PostCommentRequestDto postCommentRequestDto) {
        postCommentService.updatePostComment(commentId, postCommentRequestDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/api/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable long commentId) {
        postCommentService.deletePostComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
