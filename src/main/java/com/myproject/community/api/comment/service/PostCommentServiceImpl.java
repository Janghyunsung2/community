package com.myproject.community.api.comment.service;

import com.myproject.community.api.auth.jwt.JwtProvider;
import com.myproject.community.api.comment.dto.PostCommentResponseDto;
import com.myproject.community.api.comment.repository.CommentRepository;
import com.myproject.community.api.comment.repository.PostCommentRepository;
import com.myproject.community.api.comment.dto.PostCommentRequestDto;
import com.myproject.community.api.comment.dto.PostCommentResponseGroupDto;
import com.myproject.community.api.member.repository.MemberRepository;
import com.myproject.community.api.post.repository.PostRepository;
import com.myproject.community.domain.comment.Comment;
import com.myproject.community.domain.member.Member;
import com.myproject.community.domain.post.Post;
import com.myproject.community.domain.post.PostComment;
import com.myproject.community.error.CustomException;
import com.myproject.community.error.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostCommentServiceImpl implements PostCommentService {

    private final CommentRepository commentRepository;
    private final PostCommentRepository postCommentRepository;
    private final PostRepository postRepository;
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    @Transactional
    public void postCommentSave(long postId, PostCommentRequestDto postCommentRequestDto, HttpServletRequest request) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new CustomException(
                ErrorCode.POST_NOT_FOUND));
        long authUserId = jwtProvider.getAuthUserId(request);

        Member member = memberRepository.findById(authUserId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        Comment parent = commentRepository.findById(postCommentRequestDto.getParentId())
            .orElse(null);
        Comment comment = Comment.builder().content(postCommentRequestDto.getContent())
            .parent(parent).build();
        PostComment postComment = PostComment.builder().comment(comment).post(post).member(member)
            .build();

        commentRepository.save(comment);
        postCommentRepository.save(postComment);
    }

    @Transactional
    public void updatePostComment(long commentId, PostCommentRequestDto postCommentRequestDto){
        Comment comment = getCommentById(commentId);
        comment.updateComment(postCommentRequestDto.getContent());
    }

    @Transactional
    @Override
    public void deletePostCommenMember(long commentId) {
        getCommentById(commentId).adminDelete();
    }

    @Transactional
    public void deletePostCommentMember(long commentId){
        Comment comment = getCommentById(commentId);
        comment.isDeleted();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<PostCommentResponseDto> getCommentAll(Pageable pageable) {
        return commentRepository.getPostCommentsAll(pageable);
    }

    @Override
    public void deletePostCommentAdmin(long commentId) {

    }

    private Comment getCommentById(long commentId){
        return commentRepository.findById(commentId)
            .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<PostCommentResponseGroupDto> getCommentGroupByPostId(long postId) {
        return commentRepository.findCommentByPostId(postId);
    }



}
