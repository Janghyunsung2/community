package com.myproject.community.api.post.controller;

import com.myproject.community.api.post.dto.PostDetailDto;
import com.myproject.community.api.post.dto.PostListDto;
import com.myproject.community.api.post.service.PostService;
import com.myproject.community.api.post.dto.PostUpdateDto;
import com.myproject.community.api.post.dto.PostWithBoardDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/api/boards/{boardId}/posts")
    public ResponseEntity<String> createPost(@PathVariable long boardId, @RequestBody PostWithBoardDto postWithBoardDto, HttpServletRequest request) {
        postService.createPost(boardId, postWithBoardDto, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/boards/{boardId}/posts")
    public ResponseEntity<Page<PostListDto>> getPosts(@PathVariable long boardId, Pageable pageable){
        return ResponseEntity.ok(postService.getPosts(boardId, pageable));
    }

    @GetMapping("/api/admin/posts/search")
    public ResponseEntity<Page<PostListDto>> getAdminPosts(@RequestParam String keyword, Pageable pageable){
        return ResponseEntity.ok(postService.getPostsByKeyword(keyword, pageable));
    }

    @DeleteMapping("/api/admin/posts")
    public ResponseEntity<?> deleteAdminPost(@RequestParam long id){
        postService.deleteAdminPost(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/posts/{postId}")
    public ResponseEntity<PostDetailDto> getPost(@PathVariable long postId){
        return ResponseEntity.ok(postService.getPostDetail(postId));
    }

    @PutMapping("/api/posts/{postId}")
    public ResponseEntity<Void> updatePost(@PathVariable long postId, @RequestBody PostUpdateDto postUpdateDto){
        postService.updatePost(postId ,postUpdateDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/posts/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable long postId){
        postService.deletePost(postId);
        return ResponseEntity.ok().build();
    }


}
