package com.myproject.community.api.member.service.impl;

import com.myproject.community.api.account.AccountRepository;
import com.myproject.community.api.auth.dto.MemberAuthDto;
import com.myproject.community.api.auth.dto.MemberAuthDto.MemberAccount;
import com.myproject.community.api.auth.jwt.JwtProvider;
import com.myproject.community.api.member.dto.MemberAdminResponseDto;
import com.myproject.community.api.member.dto.MemberResponseDto;
import com.myproject.community.api.member.dto.MemberUpdateDto;
import com.myproject.community.api.member.repository.GenderRepository;
import com.myproject.community.api.member.repository.MemberRepository;
import com.myproject.community.api.member.dto.MemberCreateDto;
import com.myproject.community.api.member.repository.MemberStatusRepository;
import com.myproject.community.api.member.service.MemberService;
import com.myproject.community.domain.account.Account;
import com.myproject.community.domain.account.Role;
import com.myproject.community.domain.gender.Gender;
import com.myproject.community.domain.member.Member;
import com.myproject.community.domain.member.MemberStatus;
import com.myproject.community.domain.member.Status;
import com.myproject.community.error.CustomException;
import com.myproject.community.error.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;
    private final MemberStatusRepository memberStatusRepository;
    private final PasswordEncoder passwordEncoder;
    private final GenderRepository genderRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    @Override
    public void registerMember(MemberCreateDto dto) {

        Gender gender = genderRepository
            .findByName(dto.getGender()).orElse(genderRepository.save(new Gender(dto.getGender())));

        MemberStatus active = MemberStatus.builder()
            .status(Status.ACTIVE)
            .build();
        memberStatusRepository.save(active);

        Member member = Member.builder()
            .name(dto.getName())
            .email(dto.getEmail())
            .birthday(dto.getBirthday())
            .phoneNumber(dto.getPhone())
            .nickName(dto.getNickname())
            .gender(gender)
            .memberStatus(active)
            .build();

        Account account = Account.builder()
            .member(member)
            .username(dto.getUsername())
            .password(passwordEncoder.encode(dto.getPassword()))
            .role(Role.MEMBER)
            .build();

        memberRepository.saveAndFlush(member);
        accountRepository.save(account);

        log.info("회원가입 성공");

    }

    @Transactional
    @Override
    public void updateLastLogin(long memberId) {
        memberRepository.updateByMemberId(memberId);
    }

    @Transactional(readOnly = true)
    public MemberAuthDto getAuthMember(long memberId) {

        Account account = accountRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.QUIT_ACCOUNT));
        MemberAccount memberAccount = new MemberAccount(account.getUsername(),
            account.getPassword());
        return new MemberAuthDto(memberId, memberAccount, account.getRole());
    }


    @Transactional(readOnly = true)
    public long getMemberIdByUsername(String loginId) {
        Long memberId = memberRepository.findByUserLoginId(loginId);
        if(memberId == null) {
            throw new CustomException(ErrorCode.QUIT_ACCOUNT);
        }
        return memberId;
    }

    @Transactional
    public void updateMember(MemberUpdateDto dto, HttpServletRequest request) {
        long memberId = jwtProvider.getAuthUserId(request);
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.QUIT_ACCOUNT));

        member.updateMember(dto.getName(), dto.getNickname(), dto.getBirthday());

        Account account = accountRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.QUIT_ACCOUNT));

        account.updatePassword(passwordEncoder.encode(dto.getPassword()));
    }

    @Transactional(readOnly = true)
    public Page<MemberAdminResponseDto> getMembersByAdmin(Pageable pageable) {
        return memberRepository.getAdminPageMemberList(pageable);
    }

    @Override
    public boolean isNickNameExist(String nickName) {
        return memberRepository.existsByNickname(nickName);
    }

    @Override
    public boolean isUserNameExist(String username) {
        return accountRepository.existsByUsername(username);
    }

    @Override
    public MemberResponseDto getMyPageMember(HttpServletRequest request) {
        long memberId = jwtProvider.getAuthUserId(request);
        return memberRepository.findByMemberId(memberId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
