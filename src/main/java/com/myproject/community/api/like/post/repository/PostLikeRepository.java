package com.myproject.community.api.like.post.repository;

import com.myproject.community.domain.post.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {


    @Query("SELECT CASE WHEN COUNT(pl) > 0 THEN true ELSE false END FROM PostLike pl WHERE pl.post.id = :postId AND pl.member.id = :memberId")
    boolean existsByPostAndMember(@Param("postId") long postId, @Param("memberId") long memberId);

    @Modifying
    @Query("delete from PostLike pl where pl.member.id=:memberId and pl.post.id =:postId")
    void deleteByPostAndMember(long postId, long memberId);

    @Query("select count(pk) from PostLike pk where pk.post.id =:postId")
    Long postLikeCountByPostId(@Param("postId") long postId);
}
