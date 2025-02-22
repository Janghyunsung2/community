package com.myproject.community.api.post;

import com.myproject.community.api.auth.jwt.JwtProvider;
import com.myproject.community.api.board.repository.BoardRepository;
import com.myproject.community.api.image.PostImageService;
import com.myproject.community.api.member.repository.MemberRepository;
import com.myproject.community.api.post.repository.PostRepository;
import com.myproject.community.domain.board.Board;
import com.myproject.community.domain.member.Member;
import com.myproject.community.domain.post.Post;
import com.myproject.community.error.CustomException;
import com.myproject.community.error.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
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


    @Transactional
    public void createPost(long boardId, PostWithBoardDto postWithBoardDto, HttpServletRequest request) {

        long authUserId = jwtProvider.getAuthUserId(request);

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
            postImageService.savePostImages(post, postWithBoardDto.getImages());
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
    public PostDetailDto getPostDetail(long postId) {
        PostDetailDto postById = postRepository.findPostById(postId);
        Long viewCount = postViewCount(postId);
        if(viewCount > 0) {
            postById.setViewCount(viewCount);
        }
        return postById;
    }

    @Transactional
    public void updatePost(long postId, PostUpdateDto postUpdateDto) {
        Post post = findPostById(postId);
        post.update(postUpdateDto.getTitle(), postUpdateDto.getContent());
    }

    @Transactional
    public void deletePost(long postId) {
        Post post = findPostById(postId);
        post.authorDelete();
    }

    @Transactional
    public void deleteAdminPost(long postId) {
        Post post = findPostById(postId);
        post.authorDelete();
    }

    @Override
    public Page<PostListDto> getPostAll(Pageable pageable) {
        Page<PostListDto> postAll = postRepository.getPostAll(pageable);
        postAll.getContent().forEach(postListDto -> postListDto.setViews(getPostViewCount(postListDto.getPostId())));
        return postAll;
    }

    private Post findPostById(long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }

    private Long postViewCount(long postId) {
        String redisKey = VIEW_COUNT_PREFIX + postId;
        return redisTemplate.opsForValue().increment(redisKey);
    }

    private Long getPostViewCount(long postId) {
        String redisKey = VIEW_COUNT_PREFIX + postId;
        String viewCount = redisTemplate.opsForValue().get(redisKey);
        if(viewCount != null) {
            return Long.parseLong(viewCount);
        }
        return 0L;
    }

}
