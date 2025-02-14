package com.myproject.community.api.member.service.impl;

import static org.mockito.Mockito.*;

import com.myproject.community.api.account.AccountRepository;
import com.myproject.community.api.member.dto.MemberCreateDto;
import com.myproject.community.api.member.repository.GenderRepository;
import com.myproject.community.api.member.repository.MemberRepository;
import com.myproject.community.domain.gender.Gender;
import com.myproject.community.domain.member.Member;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private GenderRepository genderRepository;

    @InjectMocks
    private MemberServiceImpl memberServiceImpl;

    @Test
    @DisplayName("회원가입 성공")
    void registerSuccess() {

        MemberCreateDto memberRequestDto = new MemberCreateDto("james", "test", "1234", "test@gmail.com", "testNick", "010-3106-7976", "MALE", LocalDate.now(), "1236478" );

        Gender mockGender = mock(Gender.class);

        when(genderRepository.findByName(anyString())).thenReturn(Optional.of(mockGender));


        memberServiceImpl.registerMember(memberRequestDto);

        verify(memberRepository, times(1)).save(any(Member.class));

    }


}