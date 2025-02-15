package com.myproject.community.api.post;

import com.myproject.community.api.board.BoardRepository;
import com.myproject.community.api.image.PostImageService;
import com.myproject.community.api.member.repository.MemberRepository;
import com.myproject.community.api.post.repository.PostRepository;
import com.myproject.community.domain.board.Board;
import com.myproject.community.domain.member.Member;
import com.myproject.community.domain.post.Post;
import com.myproject.community.error.CustomException;
import com.myproject.community.error.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final PostImageService postImageService;



    @Transactional
    public void createPost(PostWithBoardDto postWithBoardDto) {

        long authorId = postWithBoardDto.getAuthorId();
        long boardId = postWithBoardDto.getBoardId();

        Member member = memberRepository.findById(authorId)
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

        postImageService.savePostImages(post, postWithBoardDto.getImages());

    }

    @Transactional(readOnly = true)
    @Override
    public Page<PostListDto> getPosts(long boardId, Pageable pageable) {
        return postRepository.findPostsByBoardId(boardId, pageable);
    }

    @Transactional(readOnly = true)
    public PostDetailDto getPostDetail(long postId) {
        return postRepository.findPostById(postId);
    }

    @Transactional
    public void updatePost(PostUpdateDto postUpdateDto) {
        long postId = postUpdateDto.getPostId();
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        post.update(postUpdateDto.getTitle(), postUpdateDto.getContent());
    }

    @Transactional
    public void deletePost(long postId) {
        postRepository.deleteById(postId);
    }

}
