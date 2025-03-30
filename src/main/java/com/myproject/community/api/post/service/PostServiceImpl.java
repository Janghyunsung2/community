package com.myproject.community.api.post.service;

import com.myproject.community.api.account.AccountRepository;
import com.myproject.community.api.auth.jwt.JwtProvider;
import com.myproject.community.api.board.repository.BoardRepository;
import com.myproject.community.api.image.PostImageService;
import com.myproject.community.api.member.repository.MemberRepository;
import com.myproject.community.api.post.dto.BestPostDto;
import com.myproject.community.api.post.dto.PeriodType;
import com.myproject.community.api.post.dto.PostUpdateDto;
import com.myproject.community.api.post.dto.PostViewRankingDto;
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
import java.time.Duration;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
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

        return postRepository.findPostsByBoardId(boardId, pageable);
    }

    @Transactional(readOnly = true)
    public PostDetailDto getPostDetail(long postId, HttpServletRequest request) {
        PostDetailDto postById = postRepository.findPostById(postId);
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

    @Transactional(readOnly = true)
    @Override
    public Page<PostListDto> getPostsByKeyword(String keyword, Pageable pageable) {
        Page<PostListDto> posts = postRepository.findPostsByKeyword(keyword, pageable);
        return posts;
    }


    @Override
    public void viewCount(long postId) {
        postRepository.incrementViewCount(postId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<PostViewRankingDto> getPostViewRanking(PeriodType period) {
        return postRepository.findPostViewRankByDate(period);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BestPostDto> getBestPosts(Long boardId) {
        return postRepository.findBestPostByBoardId(boardId);
    }

    private Post findPostById(long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
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


}
