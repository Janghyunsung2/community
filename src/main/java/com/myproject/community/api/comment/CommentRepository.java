package com.myproject.community.api.comment;

import com.myproject.community.api.comment.querydsl.CommentRepositoryCustom;
import com.myproject.community.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

}
