package com.myproject.community.domain.member;

import com.myproject.community.domain.gender.Gender;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String phoneNumber;

    private String email;

    private String nickName;

    private LocalDate birthday;

    private LocalDate createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private MemberStatus memberStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    private Gender gender;

}
