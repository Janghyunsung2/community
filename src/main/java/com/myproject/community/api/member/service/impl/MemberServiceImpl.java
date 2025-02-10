package com.myproject.community.api.member.service.impl;

import com.myproject.community.api.account.AccountRepository;
import com.myproject.community.api.member.repository.GenderRepository;
import com.myproject.community.api.member.repository.MemberRepository;
import com.myproject.community.api.member.dto.MemberRequestDto;
import com.myproject.community.api.member.service.MemberService;
import com.myproject.community.domain.account.Account;
import com.myproject.community.domain.account.Role;
import com.myproject.community.domain.gender.Gender;
import com.myproject.community.domain.member.Member;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final GenderRepository genderRepository;

    @Override
    public void registerMember(MemberRequestDto dto) {

        Gender gender = genderRepository
            .findByName(dto.getGender()).orElse( new Gender(dto.getGender()));


        Member member = Member.builder()
            .name(dto.getName())
            .email(dto.getEmail())
            .createDate(LocalDate.now())
            .birthday(dto.getBirthday())
            .phoneNumber(dto.getPhone())
            .nickName(dto.getNickname())
            .gender(gender)
            .build();
        memberRepository.save(member);

        Account account = Account.builder()
            .member(member)
            .username(dto.getUsername())
            .password(dto.getPassword())
            .role(Role.MEMBER)
            .build();

        accountRepository.save(account);

    }
}
