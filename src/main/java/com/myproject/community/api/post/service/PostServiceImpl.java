package com.myproject.community.api.post.service;

import com.myproject.community.api.account.AccountRepository;
import com.myproject.community.api.auth.jwt.JwtProvider;
import com.myproject.community.api.board.repository.BoardRepository;
import com.myproject.community.api.image.PostImageService;
import com.myproject.community.api.member.repository.MemberRepository;
import com.myproject.community.api.post.dto.PostUpdateDto;
import com.myproject.community.api.post.dto.PostWithBoardDto;
import com.myproject.community.api.post.dto.PostDetailDto;
import com.myproject.community.api.post.dto.PostListDto;
import com.myproject.community.api.post.repository.PostRepository;
import com.myproject.community.domain.account.Account;
import com.myproject.community.domain.account.Role;
import com.myproject.community.domain.board.Board;
import com.myproject.community.domain.member.Member;
import com.myproject.community.domain.post.Post;
import com.myproject.community.error.CustomException;
import com.myproject.community.error.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final PostImageService postImageService;
    private final JwtProvider jwtProvider;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String VIEW_COUNT_PREFIX = "post:view:";
    private final AccountRepository accountRepository;


    @Transactional
    public void createPost(long boardId, PostWithBoardDto postWithBoardDto, HttpServletRequest request) {

        long authUserId = getTokenMemberId(request);

        Member member = memberRepository.findById(authUserId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        Board board = boardRepository.findById(boardId)
            .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        Post post = Post.builder()
            .title(postWithBoardDto.getTitle())
            .content(postWithBoardDto.getContent())
            .member(member)
            .board(board)
            .build();
        postRepository.save(post);
        List<MultipartFile> images = postWithBoardDto.getImages();
        if(images != null && !images.isEmpty()) {
            postImageService.savePostImages(post, images);
        }


    }

    @Transactional(readOnly = true)
    @Override
    public Page<PostListDto> getPosts(long boardId, Pageable pageable) {

        Page<PostListDto> postsByBoardId = postRepository.findPostsByBoardId(boardId, pageable);

        postsByBoardId.getContent().forEach(postListDto -> postListDto.setViews(getPostViewCount(postListDto.getPostId())));

        return postsByBoardId;
    }

    @Transactional(readOnly = true)
    public PostDetailDto getPostDetail(long postId, HttpServletRequest request) {
        PostDetailDto postById = postRepository.findPostById(postId);
        Long viewCount = postViewCount(postId, request);
        if(viewCount > 0) {
            postById.setViewCount(viewCount);
        }
        return postById;
    }

    @Transactional
    public void updatePost(long postId, PostUpdateDto postUpdateDto, HttpServletRequest request) {
        Long memberId = getTokenMemberId(request);
        Member member = getMemberById(memberId);

        Post post = findPostById(postId);
        if(isSameMember(member, post)) {
            post.update(postUpdateDto.getTitle(), postUpdateDto.getContent());
        }else {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        List<MultipartFile> images = postUpdateDto.getImages();
        if(images != null && !images.isEmpty()) {
            postImageService.savePostImages(post, images);
        }
    }

    @Transactional
    public void deletePost(long postId, HttpServletRequest request) {
        Post post = findPostById(postId);
        Long memberId = getTokenMemberId(request);
        Member member = getMemberById(memberId);
        if(isSameMember(member, post)) {
            post.authorDelete();
        }else {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }

    @Transactional
    public void deleteAdminPost(long postId, HttpServletRequest request) {
        Post post = findPostById(postId);
        long adminId = getTokenMemberId(request);
        Account account = accountRepository.findById(adminId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        Role role = account.getRole();
        if(role.equals(Role.ADMIN)) {
            post.authorDelete();
        }else {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }

    @Override
    public Page<PostListDto> getPostsByKeyword(String keyword, Pageable pageable) {
        Page<PostListDto> posts = postRepository.findPostsByKeyword(keyword, pageable);
        posts.getContent().forEach(postListDto -> postListDto.setViews(getPostViewCount(postListDto.getPostId())));
        return posts;
    }

    private Post findPostById(long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }

    private Long postViewCount(long postId, HttpServletRequest request) {
        String clientIp = getClientIp(request);
        String viewCountKey = VIEW_COUNT_PREFIX + postId + ":" + clientIp;
        String redisKey = VIEW_COUNT_PREFIX + postId;

        if(Boolean.FALSE.equals(redisTemplate.hasKey(viewCountKey))) {
            Long viewCount = redisTemplate.opsForValue().increment(redisKey);
            redisTemplate.opsForValue().set(viewCountKey, "true");
            return viewCount;
        }
        String viewCountStr = redisTemplate.opsForValue().get(redisKey);
        Long viewCount = viewCountStr != null ? Long.parseLong(viewCountStr) : 0L;
        return viewCount;
    }

    private Long getPostViewCount(long postId) {
        String redisKey = VIEW_COUNT_PREFIX + postId;
        String viewCount = redisTemplate.opsForValue().get(redisKey);
        if(viewCount != null) {
            return Long.parseLong(viewCount);
        }
        return 0L;
    }

    private boolean isSameMember(Member member, Post post) {
        return post.getMember().equals(member);
    }

    private Member getMemberById(long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private Long getTokenMemberId(HttpServletRequest request) {
        return jwtProvider.getAuthUserId(request);
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;

    }

}
