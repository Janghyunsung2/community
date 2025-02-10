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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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

    @Builder
    public Member(String name, String phoneNumber, String email, String nickName, LocalDate birthday, LocalDate createDate, Gender gender) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.nickName = nickName;
        this.birthday = birthday;
        this.createDate = createDate;
        this.gender = gender;
    }



}
