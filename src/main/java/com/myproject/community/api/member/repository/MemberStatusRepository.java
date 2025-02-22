package com.myproject.community.api.member.repository;

import com.myproject.community.domain.member.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberStatusRepository extends JpaRepository<MemberStatus, Long> {

}
