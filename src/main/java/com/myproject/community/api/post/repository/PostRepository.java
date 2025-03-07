package com.myproject.community.api.post.repository;

import com.myproject.community.api.post.dto.PostListDto;
import com.myproject.community.api.post.repository.querydsl.PostRepositoryCustom;
import com.myproject.community.domain.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    @Query("SELECT new com.myproject.community.api.post.dto.PostListDto(p.id, p.title, m.nickName, p.createdAt) " +
        "FROM Post p JOIN Member m ON m.id = p.member.id " +
        "WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
        "OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
        "OR LOWER(p.member.nickName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<PostListDto> findPostsByKeyword(String keyword, Pageable pageable);
}
