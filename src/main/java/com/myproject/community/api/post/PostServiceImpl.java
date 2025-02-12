package com.myproject.community.api.post;

import com.myproject.community.api.board.BoardRepository;
import com.myproject.community.api.member.repository.MemberRepository;
import com.myproject.community.domain.board.Board;
import com.myproject.community.domain.member.Member;
import com.myproject.community.domain.post.Post;
import com.myproject.community.error.CustomException;
import com.myproject.community.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;


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
    }
}
