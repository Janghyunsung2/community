package com.myproject.community.api.post.repository;

import com.myproject.community.api.post.repository.querydsl.PostRepositoryCustom;
import com.myproject.community.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {


}
