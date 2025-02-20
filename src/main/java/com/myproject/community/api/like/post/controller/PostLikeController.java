package com.myproject.community.api.like.post.controller;

import com.myproject.community.api.like.LikeCountResponseDto;
import com.myproject.community.api.like.post.service.PostLikeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostLikeController {

    private final PostLikeService postLikeService;

    @GetMapping("/api/posts/{postId}/likes/check")
    public ResponseEntity<Boolean> likesCheck(@PathVariable Long postId, HttpServletRequest request) {
        boolean postLiked = postLikeService.isPostLiked(postId, request);
        log.warn(postLiked ? "Post liked " : "Post NOT liked ");
        return ResponseEntity.ok(postLiked);
    }

    @PostMapping("/api/posts/{postId}/likes")
    public ResponseEntity<Void> postLikeCount(@PathVariable Long postId, HttpServletRequest request) {
        postLikeService.savePostLike(postId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/posts/{postId}/likes")
    public ResponseEntity<Void> deletePostLike(@PathVariable Long postId, HttpServletRequest request) {
        postLikeService.deletePostLike(postId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/posts/{postId}/likes")
    public ResponseEntity<LikeCountResponseDto> getPostLikeCount(@PathVariable Long postId) {
        return ResponseEntity.ok(postLikeService.getPostLikeCount(postId));
    }
}
