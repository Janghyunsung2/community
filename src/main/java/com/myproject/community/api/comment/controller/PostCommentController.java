package com.myproject.community.api.comment.controller;

import com.myproject.community.api.comment.dto.PostCommentRequestDto;
import com.myproject.community.api.comment.dto.PostCommentResponseGroupDto;
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
import org.springframework.web.filter.RequestContextFilter;

@RestController
@RequiredArgsConstructor
public class PostCommentController {

    private final PostCommentService postCommentService;
    private final RequestContextFilter requestContextFilter;

    @GetMapping("/api/posts/{post-id}/comments")
    public ResponseEntity<List<PostCommentResponseGroupDto>> getCommentByPostId(@PathVariable("post-id") Long postId) {
        return ResponseEntity.ok(postCommentService.getCommentGroupByPostId(postId));
    }

    @PostMapping("/api/posts/{post-id}/comments")
    public ResponseEntity<Void> createCommentByPostId(@PathVariable("post-id") Long postId,@Validated @RequestBody PostCommentRequestDto postCommentRequestDto, HttpServletRequest request) {
        postCommentService.postCommentSave(postId, postCommentRequestDto, request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/api/comments/{id}")
    public ResponseEntity<Void> updateComment(@PathVariable long id, @RequestBody PostCommentRequestDto postCommentRequestDto, HttpServletRequest request) {
        postCommentService.updatePostComment(id, postCommentRequestDto, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/api/comments/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable long id, HttpServletRequest request) {
        postCommentService.deletePostCommentMember(id, request);
        return ResponseEntity.noContent().build();
    }
}
