package com.myproject.community.api.like.post.service;

import com.myproject.community.api.auth.jwt.JwtProvider;
import com.myproject.community.api.like.LikeCountResponseDto;
import com.myproject.community.api.like.post.repository.PostLikeRepository;
import com.myproject.community.api.member.repository.MemberRepository;
import com.myproject.community.api.post.repository.PostRepository;
import com.myproject.community.domain.member.Member;
import com.myproject.community.domain.post.Post;
import com.myproject.community.domain.post.PostLike;
import com.myproject.community.error.CustomException;
import com.myproject.community.error.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeServiceImpl implements PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final JwtProvider jwtProvider;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public void savePostLike(long postId, HttpServletRequest request) {

        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        long memberId = jwtProvider.getAuthUserId(request);
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        PostLike postLike = PostLike.builder()
            .post(post)
            .member(member)
            .build();
        boolean existsed = postLikeRepository.existsByPostAndMember(postId, memberId);
        if (!existsed) {
            postLikeRepository.save(postLike);
        }
    }

    @Transactional
    @Override
    public void deletePostLike(long postId, HttpServletRequest request) {
        long memberId = jwtProvider.getAuthUserId(request);
        postLikeRepository.deleteByPostAndMember(postId, memberId);
    }

    @Transactional(readOnly = true)
    @Override
    public LikeCountResponseDto getPostLikeCount(long postId) {
        Long count = postLikeRepository.postLikeCountByPostId(postId);
        return new LikeCountResponseDto(count);
    }

    @Override
    public boolean isPostLiked(long postId, HttpServletRequest request) {
        long memberId = jwtProvider.getAuthUserId(request);
        return postLikeRepository.existsByPostAndMember(postId, memberId);
    }
}
