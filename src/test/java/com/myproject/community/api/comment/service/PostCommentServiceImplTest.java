package com.myproject.community.api.comment.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.myproject.community.api.auth.jwt.JwtProvider;
import com.myproject.community.api.comment.dto.PostCommentRequestDto;
import com.myproject.community.api.comment.dto.PostCommentResponseDto;
import com.myproject.community.api.comment.dto.PostCommentResponseGroupDto;
import com.myproject.community.api.comment.repository.CommentRepository;
import com.myproject.community.api.comment.repository.PostCommentRepository;
import com.myproject.community.api.member.repository.MemberRepository;
import com.myproject.community.api.post.repository.PostRepository;
import com.myproject.community.domain.comment.Comment;
import com.myproject.community.domain.member.Member;
import com.myproject.community.domain.post.Post;
import com.myproject.community.domain.post.PostComment;
import com.myproject.community.error.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class PostCommentServiceImplTest {

    @Mock
    private PostCommentRepository postCommentRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private PostCommentServiceImpl postCommentService;


    @Test
    @DisplayName("게시물 댓글 저장")
    void postCommentCreateTest(){
        long postId = 1L;
        Post post = mock(Post.class);
        long authorId = 1L;
        Member member = mock(Member.class);
        PostCommentRequestDto requestDto = mock(PostCommentRequestDto.class);

        when(jwtProvider.getAuthUserId(request)).thenReturn(authorId);
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(memberRepository.findById(authorId)).thenReturn(Optional.of(member));


        postCommentService.postCommentSave(postId, requestDto, request);

        verify(postCommentRepository).save(any(PostComment.class));
        verify(commentRepository).save(any(Comment.class));

    }

    @Test
    @DisplayName("게시물 댓글 업데이트 성공")
    void postCommentUpdateTest(){
        long commentId = 1L;
        PostCommentRequestDto requestDto = mock(PostCommentRequestDto.class);
        Comment comment = mock(Comment.class);


        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(postCommentRepository.existsByMemberId(anyLong())).thenReturn(true);
        postCommentService.updatePostComment(commentId, requestDto, request);
        verify(commentRepository).findById(commentId);
    }

    @Test
    @DisplayName("게시물 댓글 업데이트 실패")
    void postCommentUpdateFailTest(){
        long commentId = 1L;
        PostCommentRequestDto requestDto = mock(PostCommentRequestDto.class);
        Comment comment = mock(Comment.class);


        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(postCommentRepository.existsByMemberId(anyLong())).thenReturn(false);
        assertThrows(
            CustomException.class ,() -> postCommentService.updatePostComment(commentId, requestDto, request));
    }

    @Test
    @DisplayName("게시물 댓글 삭제 성공")
    void postCommentDeleteTest(){
        long commentId = 1L;
        Comment comment = mock(Comment.class);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(postCommentRepository.existsByMemberId(anyLong())).thenReturn(true);

        postCommentService.deletePostCommentMember(commentId, request);
        postCommentService.deletePostCommentAdmin(commentId, request);

    }

    @Test
    @DisplayName("게시물 댓글 검색")
    void postCommentSearchTest(){
        String keyword = "test";
        Pageable pageable = mock(Pageable.class);
        Page<PostCommentResponseDto> postCommentPage = mock(Page.class);

        when(commentRepository.getPostCommentsByKeyword(keyword, pageable)).thenReturn(postCommentPage);

        Page<PostCommentResponseDto> commentByKeyword = postCommentService.getCommentByKeyword(
            keyword, pageable);

        assertEquals(postCommentPage, commentByKeyword);
    }

    @Test
    @DisplayName("게시물 댓글 그룹조회")
    void postCommentGroupSearchTest(){
        long postId = 1L;
        List<PostCommentResponseGroupDto> postCommentResponseGroupDtos = mock(List.class);

        when(commentRepository.findCommentByPostId(postId)).thenReturn(postCommentResponseGroupDtos);

        List<PostCommentResponseGroupDto> commentGroupByPostId = postCommentService.getCommentGroupByPostId(
            postId);
        assertEquals(postCommentResponseGroupDtos, commentGroupByPostId);
    }

}