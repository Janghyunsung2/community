package com.myproject.community.api.member.repository;

import com.myproject.community.domain.gender.Gender;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenderRepository extends JpaRepository<Gender, Long> {

    Optional<Gender> findByName(String name);

}
