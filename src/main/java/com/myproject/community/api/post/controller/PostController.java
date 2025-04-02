package com.myproject.community.api.post.controller;

import com.myproject.community.api.post.dto.BestPostDto;
import com.myproject.community.api.post.dto.PeriodType;
import com.myproject.community.api.post.dto.PostDetailDto;
import com.myproject.community.api.post.dto.PostListDto;
import com.myproject.community.api.post.dto.PostListViewDto;
import com.myproject.community.api.post.dto.PostViewRankingDto;
import com.myproject.community.api.post.service.PostService;
import com.myproject.community.api.post.dto.PostUpdateDto;
import com.myproject.community.api.post.dto.PostWithBoardDto;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/api/boards/{board-id}/posts")
    public ResponseEntity<String> createPost(@PathVariable(name = "board-id") long boardId, @RequestPart PostWithBoardDto postWithBoardDto,
        List<MultipartFile> images, HttpServletRequest request) {
        postWithBoardDto.setImages(images);
        postService.createPost(boardId, postWithBoardDto, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/boards/{board-id}/posts")
    public ResponseEntity<PostListViewDto> getPosts(@PathVariable("board-id") long boardId, Pageable pageable){
        return ResponseEntity.ok(postService.getPosts(boardId, pageable));
    }

    @GetMapping("/api/admin/posts/search")
    public ResponseEntity<Page<PostListDto>> getAdminPosts(@RequestParam String keyword, Pageable pageable){
        return ResponseEntity.ok(postService.getPostsByKeyword(keyword, pageable));
    }

    @DeleteMapping("/api/admin/posts")
    public ResponseEntity<?> deleteAdminPost(@RequestParam long id, HttpServletRequest request){
        postService.deleteAdminPost(id, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/posts/{post-id}")
    public ResponseEntity<PostDetailDto> getPost(@PathVariable("post-id") long postId, HttpServletRequest request){
        postService.viewCount(postId);
        return ResponseEntity.ok(postService.getPostDetail(postId, request));
    }

    @PutMapping("/api/posts/{post-id}")
    public ResponseEntity<Void> updatePost(@PathVariable("post-id") long postId, @RequestPart PostUpdateDto postUpdateDto, HttpServletRequest request){
        postService.updatePost(postId ,postUpdateDto, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/posts/{post-id}")
    public ResponseEntity<Void> deletePost(@PathVariable("post-id") long postId, HttpServletRequest request){
        postService.deletePost(postId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/posts/views")
    public ResponseEntity<List<PostViewRankingDto>> getPostViews(@RequestParam String period){
        PeriodType periodType = PeriodType.from(period);
        return ResponseEntity.ok(postService.getPostViewRanking(periodType));
    }

    @GetMapping("/api/boards/{board-id}/best")
    public ResponseEntity<List<BestPostDto>> getBestPosts(@PathVariable(name = "board-id") long boardId){
        return ResponseEntity.ok(postService.getBestPosts(boardId));
    }


}
