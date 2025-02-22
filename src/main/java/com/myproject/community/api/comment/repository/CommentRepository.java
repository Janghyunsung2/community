package com.myproject.community.api.comment.repository;

import com.myproject.community.api.comment.repository.querydsl.CommentRepositoryCustom;
import com.myproject.community.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

}
