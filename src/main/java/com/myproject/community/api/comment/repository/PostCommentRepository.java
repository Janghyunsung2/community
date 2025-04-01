package com.myproject.community.api.comment.repository;

import com.myproject.community.domain.post.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {

    boolean existsByMemberId(Long memberId);
}
