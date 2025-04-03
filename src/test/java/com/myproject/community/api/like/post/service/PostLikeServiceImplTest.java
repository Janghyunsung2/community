package com.myproject.community.api.like.post.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.myproject.community.api.auth.jwt.JwtProvider;
import com.myproject.community.api.like.LikeCountResponseDto;
import com.myproject.community.api.like.post.repository.PostLikeRepository;
import com.myproject.community.api.member.repository.MemberRepository;
import com.myproject.community.api.post.repository.PostRepository;
import com.myproject.community.domain.member.Member;
import com.myproject.community.domain.post.Post;
import com.myproject.community.domain.post.PostLike;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostLikeServiceImplTest {
    @Mock
    private PostRepository postRepository;
    @Mock
    private MemberRepository memberRepository;

    @Mock
    HttpServletRequest request;
    @Mock
    PostLikeRepository postLikeRepository;

    @Mock
    JwtProvider jwtProvider;

    @InjectMocks
    PostLikeServiceImpl postLikeService;


    @Test
    @DisplayName("게시물좋아요 저장")
    void postLikeCreateTest(){

        long postId = 1L;
        long memberId = 2L;

        Post post = mock(Post.class);
        Member member = mock(Member.class);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(jwtProvider.getAuthUserId(request)).thenReturn(memberId);

        when(postLikeRepository.existsByPostAndMember(postId, memberId)).thenReturn(false);

       postLikeService.savePostLike(postId, request);

       verify(postLikeRepository).save(any(PostLike.class));
       verify(postLikeRepository, times(1)).save(any(PostLike.class));

    }

    @Test
    @DisplayName("게시물좋아요 취소")
    void postLikeDeleteTest(){
        long postId = 1L;
        long memberId = 2L;

        when(jwtProvider.getAuthUserId(request)).thenReturn(memberId);

        postLikeService.deletePostLike(postId,request);

        verify(postLikeRepository, times(1)).deleteByPostAndMember(anyLong(), anyLong());
    }

    @Test
    @DisplayName("게시물 좋아요 갯수 조회")
    void postLikeCountTest(){
        long postId = 1L;
        when(postLikeRepository.postLikeCountByPostId(postId)).thenReturn(1l);

        LikeCountResponseDto postLikeCount = postLikeService.getPostLikeCount(postId);
        assertEquals(1l, postLikeCount.getLikeCount());
    }

    @Test
    @DisplayName("좋아요 누른 회원인지 확인")
    void isPostLikeTest(){
        long postId = 1L;
        long memberId = 2L;
        when(jwtProvider.getAuthUserId(request)).thenReturn(memberId);
        when(postLikeRepository.existsByPostAndMember(postId, memberId)).thenReturn(true);
        boolean postLiked = postLikeService.isPostLiked(postId, request);

        assertTrue(postLiked);
    }

}