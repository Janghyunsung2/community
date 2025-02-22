package com.myproject.community.api.post.repository;

import com.myproject.community.api.post.PostListDto;
import com.myproject.community.api.post.repository.querydsl.PostRepositoryCustom;
import com.myproject.community.domain.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    @Query("select new com.myproject.community.api.post.PostListDto(p.id, p.title, m.nickName, p.createdAt) from Post p join Member m on m.id = p.member.id")
    Page<PostListDto> getPostAll(Pageable pageable);
}
