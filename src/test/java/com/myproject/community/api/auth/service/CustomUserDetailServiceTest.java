package com.myproject.community.api.auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.myproject.community.api.account.AccountRepository;
import com.myproject.community.domain.account.Account;
import com.myproject.community.domain.account.Role;
import com.myproject.community.domain.member.Member;
import com.myproject.community.error.CustomException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailServiceTest {

    @Mock
    AccountRepository accountRepository;
    @InjectMocks
    CustomUserDetailService customUserDetailService;

    @Test
    @DisplayName("custom유저 검증")
    void customUserDetailServiceTest() {
        Account account = Account.builder()
                .username("admin")
                    .password("1234")
                        .role(Role.ADMIN)
            .member(Member.builder().name("test").build())
                            .build();

        when(accountRepository.findByUsername("admin")).thenReturn(Optional.of(account));

        UserDetails userDetails = customUserDetailService.loadUserByUsername("admin");

        assertNotNull(userDetails);
        assertEquals("admin", userDetails.getUsername());
    }

    @Test
    @DisplayName("custom유저 실패")
    void customUserDetailServiceFailTest() {
        when(accountRepository.findByUsername("admin")).thenReturn(Optional.empty());
        assertThrows(CustomException.class, () -> customUserDetailService.loadUserByUsername("admin"));
    }

}