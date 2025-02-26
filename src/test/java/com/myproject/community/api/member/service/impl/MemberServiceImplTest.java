package com.myproject.community.api.member.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.myproject.community.api.account.AccountRepository;
import com.myproject.community.api.auth.dto.MemberAuthDto.MemberAccount;
import com.myproject.community.api.auth.jwt.JwtProvider;
import com.myproject.community.api.member.dto.MemberAdminResponseDto;
import com.myproject.community.api.member.dto.MemberCreateDto;
import com.myproject.community.api.member.dto.MemberResponseDto;
import com.myproject.community.api.member.dto.MemberUpdateDto;
import com.myproject.community.api.member.repository.GenderRepository;
import com.myproject.community.api.member.repository.MemberRepository;
import com.myproject.community.api.member.repository.MemberStatusRepository;
import com.myproject.community.domain.account.Account;
import com.myproject.community.domain.account.Role;
import com.myproject.community.domain.gender.Gender;
import com.myproject.community.domain.member.Member;
import com.myproject.community.domain.member.MemberStatus;
import com.myproject.community.domain.member.Status;
import com.myproject.community.error.CustomException;
import com.myproject.community.error.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @Mock
    private MemberStatusRepository memberStatusRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private HttpServletRequest request;
    @Test
    @DisplayName("회원가입 성공")
    void registerSuccess() {

        MemberCreateDto memberRequestDto = new MemberCreateDto("james", "test", "1234", "test@gmail.com", "testNick", "010-3106-7976", "MALE", LocalDate.now(), "1236478");

        Gender mockGender = Gender.builder().name("MALE").build();

        MemberStatus memberStatus = MemberStatus.builder().status(Status.ACTIVE).build();

        Member member = Member.builder()
            .name("james")
            .email("test@gmail.com")
            .birthday(LocalDate.now())
            .phoneNumber("010-1234-5678")
            .nickName("nick")
            .gender(mockGender)
            .memberStatus(memberStatus)
            .build();

        Account account = Account.builder()
            .username("james")
            .password(passwordEncoder.encode("1234"))
            .role(Role.MEMBER)
            .member(member)
            .build();

        // Stubbing
        when(genderRepository.findByName(mockGender.getName())).thenReturn(Optional.of(mockGender));
        doReturn(memberStatus).when(memberStatusRepository).save(any(MemberStatus.class));
        doReturn(member).when(memberRepository).saveAndFlush(any(Member.class));
        doReturn(account).when(accountRepository).save(any(Account.class));

        // 테스트 실행
        memberServiceImpl.registerMember(memberRequestDto);


        // 검증
        verify(memberRepository, times(1)).saveAndFlush(any(Member.class));
        verify(memberStatusRepository, times(1)).save(any(MemberStatus.class));
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    @DisplayName("마지막 로그인 업데이트")
    void registerFail() {
        long memeberId = 1L;

        doNothing().when(memberRepository).updateByMemberId(memeberId);

        memberServiceImpl.updateLastLogin(memeberId);

        verify(memberRepository, times(1)).updateByMemberId(memeberId);
    }

    @Test
    @DisplayName("인증맴버 요청")
    void authMemberSuccess() {
        Account account = mock(Account.class);
        MemberAccount memberAccount = mock(MemberAccount.class);
        long memberId = 1L;

        when(accountRepository.findById(memberId)).thenReturn(Optional.of(account));

        memberServiceImpl.getAuthMember(memberId);

        verify(accountRepository, times(1)).findById(memberId);
    }

    @Test
    @DisplayName("회원 인증 실패 - 존재하지 않는 회원")
    void getAuthMemberNotFoundTest() {
        // Given
        long memberId = 1L;

        // accountRepository에서 해당 memberId로 검색 시 Optional.empty() 반환
        when(accountRepository.findById(memberId)).thenReturn(Optional.empty());

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            memberServiceImpl.getAuthMember(memberId);
        });

        // 예외 메시지 및 코드 검증
        assertEquals(ErrorCode.MEMBER_NOT_FOUND, exception.getErrorCode());
        assertEquals("맴버를 찾을 수 없음", exception.getErrorCode().getDetail());

        // Repository 호출 검증
        verify(accountRepository, times(1)).findById(memberId);
    }

    @Test
    @DisplayName("맴버이름으로 맴버 조회")
    void memberIdByUsernameSuccess() {
        String username = "james";
        long memberId = 1L;
        when(memberRepository.findByUserLoginId(username)).thenReturn(memberId);

        memberServiceImpl.getMemberIdByUsername(username);

        verify(memberRepository, times(1)).findByUserLoginId(username);
    }

    @Test
    @DisplayName("맴버이름으로 맴버 조회 실패")
    void memberIdByUsernameFail() {
        String username = "james";
        Long memberId = null;
        when(memberRepository.findByUserLoginId(username)).thenReturn(memberId);
        CustomException exception = assertThrows(CustomException.class, () -> {
            memberServiceImpl.getMemberIdByUsername(username);
        });
        assertEquals(ErrorCode.MEMBER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("회원 정보 업데이트 성공")
    void updateMemberSuccess() {
        // Given
        long memberId = 1L;
        MemberUpdateDto updateDto = new MemberUpdateDto("New Name", "New Nick", "1234", LocalDate.of(1990, 1, 1));

        Member member = Mockito.mock(Member.class);
        Account account = Mockito.mock(Account.class);

        when(jwtProvider.getAuthUserId(request)).thenReturn(memberId);
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(accountRepository.findById(memberId)).thenReturn(Optional.of(account));
        when(passwordEncoder.encode(updateDto.getPassword())).thenReturn("encodedPassword");

        // When
        memberServiceImpl.updateMember(updateDto, request);

        // Then
        verify(memberRepository, times(1)).findById(memberId);
        verify(member, times(1)).updateMember(updateDto.getName(), updateDto.getNickname(), updateDto.getBirthday());

        verify(accountRepository, times(1)).findById(memberId);
        verify(account, times(1)).updatePassword("encodedPassword");
    }

    @Test
    @DisplayName("회원 정보 업데이트 실패 - 회원 정보 없음")
    void updateMemberMemberNotFound() {
        // Given
        long memberId = 1L;
        MemberUpdateDto updateDto = new MemberUpdateDto("New Name", "New Nick", "1234", LocalDate.of(1990, 1, 1));

        when(jwtProvider.getAuthUserId(request)).thenReturn(memberId);
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            memberServiceImpl.updateMember(updateDto, request);
        });

        assertEquals(ErrorCode.MEMBER_NOT_FOUND, exception.getErrorCode());
        verify(memberRepository, times(1)).findById(memberId);
    }

    @Test
    @DisplayName("비밀번호 변경 실패 - 계정 없음")
    void updateMemberAccountNotFound() {
        // Given
        long memberId = 1L;
        MemberUpdateDto updateDto = new MemberUpdateDto("New Name", "New Nick", "1234", LocalDate.of(1990, 1, 1));

        Member member = Mockito.mock(Member.class);

        when(jwtProvider.getAuthUserId(request)).thenReturn(memberId);
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(accountRepository.findById(memberId)).thenReturn(Optional.empty());

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            memberServiceImpl.updateMember(updateDto, request);
        });

        assertEquals(ErrorCode.QUIT_ACCOUNT, exception.getErrorCode());
        verify(accountRepository, times(1)).findById(memberId);
    }

    @Test
    @DisplayName("관리자 페이지 맴버리스트 조회")
    void membersByAdminSuccess() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<MemberAdminResponseDto> memberAdminResponseDtoPage = Mockito.mock(Page.class);
        when(memberRepository.getAdminPageMemberList(pageable)).thenReturn(memberAdminResponseDtoPage);

        memberServiceImpl.getMembersByAdmin(pageable);
        verify(memberRepository, times(1)).getAdminPageMemberList(pageable);

    }

    @Test
    @DisplayName("닉네임 검증")
    void nickNameIsExistSuccess() {
        when(memberRepository.existsByNickname(anyString())).thenReturn(true);

        memberServiceImpl.isNickNameExist(anyString());
        verify(memberRepository, times(1)).existsByNickname(anyString());
    }

    @Test
    @DisplayName("유저 아이디 중복 검증")
    void nickNameIsExist() {
        when(accountRepository.existsByUsername(anyString())).thenReturn(true);
        memberServiceImpl.isUserNameExist(anyString());
        verify(accountRepository, times(1)).existsByUsername(anyString());
    }

    @Test
    @DisplayName("마이페이지 닉네임 조회 성공")
    void myPageMemberSuccess() {
        long memberId = 1L;
        MemberResponseDto memberResponseDto = Mockito.mock(MemberResponseDto.class);
        when(jwtProvider.getAuthUserId(request)).thenReturn(memberId);
        when(memberRepository.findByMemberId(memberId)).thenReturn(Optional.of(memberResponseDto));
        memberServiceImpl.getMyPageMember(request);
        verify(memberRepository, times(1)).findByMemberId(memberId);
    }

    @Test
    @DisplayName("마이페이지 닉네임 조회 성공")
    void myPageMemberFailed() {
        // Given
        long memberId = 1L;

        // jwtProvider에서 memberId 추출
        when(jwtProvider.getAuthUserId(request)).thenReturn(memberId);

        // memberRepository에서 해당 memberId를 찾지 못함
        when(memberRepository.findByMemberId(memberId)).thenReturn(Optional.empty());

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            memberServiceImpl.getMyPageMember(request);
        });

        // 검증
        assertEquals(ErrorCode.MEMBER_NOT_FOUND, exception.getErrorCode());

        verify(jwtProvider, times(1)).getAuthUserId(request);
        verify(memberRepository, times(1)).findByMemberId(memberId);
    }

}