package com.myproject.community.domain.account;

import com.myproject.community.domain.member.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Account {

    @Id
    private long id;

    private String username;

    private String password;

    @MapsId
    @OneToOne
    @JoinColumn(name = "id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Account(String username, String password, Role role, Member member) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.member = member;
        this.id = member.getId();
    }

}
