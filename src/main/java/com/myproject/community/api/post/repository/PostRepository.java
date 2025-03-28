package com.myproject.community.api.post.repository;

import com.myproject.community.api.post.dto.PostListDto;
import com.myproject.community.api.post.repository.querydsl.PostRepositoryCustom;
import com.myproject.community.domain.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    @Query("SELECT new com.myproject.community.api.post.dto.PostListDto(p.id, p.title, m.nickName, p.createdAt, p.viewCount) " +
        "FROM Post p JOIN Member m ON m.id = p.member.id " +
        "WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
        "OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
        "OR LOWER(p.member.nickName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<PostListDto> findPostsByKeyword(String keyword, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.id = :id")
    void incrementViewCount(Long id);
}
