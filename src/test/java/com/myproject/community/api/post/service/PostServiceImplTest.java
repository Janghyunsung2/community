package com.myproject.community.api.post.service;

import static org.junit.jupiter.api.Assertions.*;

import com.myproject.community.api.account.AccountRepository;
import com.myproject.community.api.auth.jwt.JwtProvider;
import com.myproject.community.api.board.repository.BoardRepository;
import com.myproject.community.api.image.PostImageService;
import com.myproject.community.api.member.repository.MemberRepository;
import com.myproject.community.api.post.dto.BestPostDto;
import com.myproject.community.api.post.dto.BoardInfoDto;
import com.myproject.community.api.post.dto.PeriodType;
import com.myproject.community.api.post.dto.PostDetailDto;
import com.myproject.community.api.post.dto.PostListDto;
import com.myproject.community.api.post.dto.PostListViewDto;
import com.myproject.community.api.post.dto.PostUpdateDto;
import com.myproject.community.api.post.dto.PostViewRankingDto;
import com.myproject.community.api.post.dto.PostWithBoardDto;
import com.myproject.community.api.post.repository.PostRepository;
import com.myproject.community.domain.account.Account;
import com.myproject.community.domain.account.Role;
import com.myproject.community.domain.board.Board;
import com.myproject.community.domain.member.Member;
import com.myproject.community.domain.post.Post;
import com.myproject.community.error.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private BoardRepository boardRepository;
    @Mock
    private PostImageService postImageService;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private JwtProvider jwtProvider;
    @Mock
    private RedisTemplate<String, String> redisTemplate;
    @Mock
    private ValueOperations<String, String> valueOperations;
    @Mock
    private HttpServletRequest request;
    @InjectMocks
    private PostServiceImpl postService;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("게시글 생성")
    void createPostSuccess() {
        long boardId = 1L;
        Member member = Member.builder().nickName("test").build();
        Board board = Board.builder().title("title").active(true).build();

        MockMultipartFile mockFile = new MockMultipartFile(
            "file",                     // 필드명 (request parameter name)
            "test.txt",                 // 파일 이름
            "text/plain",               // 파일 타입
            "Hello, World!".getBytes(StandardCharsets.UTF_8) // 파일 내용
        );

        PostWithBoardDto postWithBoardDto = PostWithBoardDto.builder()
            .title("title")
            .content("content")
            .images(List.of(mockFile))
            .authorId(1L)
            .build();
        Mockito.when(memberRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(member));
        Mockito.when(boardRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(board));

        postService.createPost(boardId, postWithBoardDto, request);

        Mockito.verify(postRepository, Mockito.times(1)).save(Mockito.any(Post.class));
    }

    @Test
    @DisplayName("게시물 목록검증")
    void getPostsSuccess() {
        long boardId = 1L;
        Pageable pageable = Pageable.ofSize(10);
        PostListDto postListDto = PostListDto.builder()
            .postId(1L)
            .title("title")
            .createAt(LocalDateTime.now())
            .nickName("test")
            .build();

        BoardInfoDto boardInfoDto = BoardInfoDto.builder().id(boardId).title("test").build();

        Page<PostListDto> postListDtos = new PageImpl<>(List.of(postListDto), pageable, 1);


        Mockito.when(boardRepository.getBoardByBoardId(boardId)).thenReturn(Optional.of(boardInfoDto));
        Mockito.when(postRepository.findPostsByBoardId(boardId, pageable)).thenReturn(postListDtos);

        PostListViewDto postListViewDto = PostListViewDto.builder().posts(postListDtos)
            .boardInfo(boardInfoDto).build();

        PostListViewDto posts = postService.getPosts(boardId, pageable);


        assertEquals(posts.getPosts(), postListViewDto.getPosts());
        assertEquals(posts.getBoardInfo(), postListViewDto.getBoardInfo());
    }




    @Test
    @DisplayName("게시물 수정 성공")
    void updatePostSuccess() {
        long postId = 1L;
        MockMultipartFile multipartFile = new MockMultipartFile(
            "file",
            "test.txt",
            "text/plain", "Hello, World!".getBytes(StandardCharsets.UTF_8));
        PostUpdateDto postUpdateDto = PostUpdateDto.builder()
            .title("title")
            .content("content")
            .postId(postId)
            .images(List.of(multipartFile))
            .build();
        Member member = Member.builder().nickName("test").build();

        Post post = Post.builder().title("title").content("content").member(member).build();

        Mockito.when(memberRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(member));
        Mockito.when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        postService.updatePost(postId, postUpdateDto, request);

        Mockito.verify(postRepository, Mockito.times(1)).findById(postId);
    }


    @Test
    @DisplayName("게시물 수정 권한없음")
    void updatePostFailure() {
        long postId = 1L;
        MockMultipartFile multipartFile = new MockMultipartFile(
            "file",
            "test.txt",
            "text/plain", "Hello, World!".getBytes(StandardCharsets.UTF_8));
        PostUpdateDto postUpdateDto = PostUpdateDto.builder()
            .title("title")
            .content("content")
            .postId(postId)
            .images(List.of(multipartFile))
            .build();
        Member member = Member.builder().nickName("test").build();
        Member forbiddenMember = Member.builder().nickName("forbidden").build();

        Post post = Post.builder().title("title").content("content").member(member).build();

        Mockito.when(memberRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(forbiddenMember));
        Mockito.when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        Assertions.assertThrows(
            CustomException.class, () -> postService.updatePost(postId, postUpdateDto, request));
    }

    @Test
    @DisplayName("게시물 회원 삭제")
    void deletePostSuccess() {
        long postId = 1L;
        Member member = Member.builder().nickName("test").build();
        Post post = Post.builder().title("title").content("content").member(member).build();

        Mockito.when(memberRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(member));
        Mockito.when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        postService.deletePost(postId, request);

        Mockito.verify(postRepository, Mockito.times(1)).findById(postId);
    }

    @Test
    @DisplayName("게시물 회원 삭제 권한없음")
    void deletePostFailure() {
        long postId = 1L;
        Member member = Member.builder().nickName("test").build();
        Member forbiddenMember = Member.builder().nickName("forbidden").build();
        Post post = Post.builder().title("title").content("content").member(member).build();

        Mockito.when(memberRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(forbiddenMember));
        Mockito.when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        Assertions.assertThrows(CustomException.class, () -> postService.deletePost(postId, request));
    }

    @Test
    @DisplayName("게시물 관리자 삭제")
    void deleteAdminPostSuccess() {
        long postId = 1L;
        long adminId = 1L;
        Member member = Member.builder().nickName("test").build();
        Post post = Post.builder()
            .title("title")
            .content("content")
            .build();
        Account account = Account.builder()
            .username("admin")
            .password("admin")
            .member(member)
            .role(Role.ADMIN)
            .build();
        Mockito.when(accountRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(account));
        Mockito.when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        postService.deleteAdminPost(postId, request);

        Mockito.verify(postRepository, Mockito.times(1)).findById(postId);
    }

    @Test
    @DisplayName("게시물 관리자 삭제 권한없음")
    void deleteAdminPostFailure() {
        long postId = 1L;
        Member member = Member.builder().nickName("test").build();
        Post post = Post.builder()
            .title("title")
            .content("content")
            .build();
        Account account = Account.builder()
            .username("admin")
            .password("admin")
            .member(member)
            .role(Role.MEMBER)
            .build();
        Mockito.when(accountRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(account));
        Mockito.when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        Assertions.assertThrows(CustomException.class, () -> postService.deleteAdminPost(postId, request));
    }

    @Test
    @DisplayName("게시물 조회수")
    void viewCountSuccess() {
        long postId = 1L;

        postService.viewCount(postId);

        Mockito.verify(postRepository, Mockito.times(1)).incrementViewCount(postId);
    }

    @Test
    @DisplayName("게시물 조회순 일간 랭킹")
    void postRankViewSuccess() {
        PeriodType daily = PeriodType.DAILY;
        long boardId = 1l;

        PostViewRankingDto postViewRankingDto = PostViewRankingDto.builder()
            .boardId(boardId)
            .title("title")
            .build();

        Mockito.when(postRepository.findPostViewRankByDate(daily)).thenReturn(List.of(postViewRankingDto));

        List<PostViewRankingDto> postViewRanking = postService.getPostViewRanking(daily);

        assertEquals(1, postViewRanking.size());
        assertEquals(boardId, postViewRanking.get(0).getBoardId());
        assertEquals("title", postViewRanking.get(0).getTitle());
    }

    @Test
    @DisplayName("추천 게시판 성공")
    void bestBoardSuccess() {
        long boardId = 1l;

        BestPostDto bestPostDto = BestPostDto.builder()
            .id(boardId)
            .title("title")
            .imageUrl("imageUrl")
            .build();

        Mockito.when(postRepository.findBestPostByBoardId(boardId)).thenReturn(List.of(bestPostDto));

        List<BestPostDto> bestPosts = postService.getBestPosts(boardId);

        assertEquals(1, bestPosts.size());
        assertEquals(bestPosts.get(0).getTitle(), bestPostDto.getTitle());
        assertEquals(bestPosts.get(0).getImageUrl(), bestPostDto.getImageUrl());
    }



}