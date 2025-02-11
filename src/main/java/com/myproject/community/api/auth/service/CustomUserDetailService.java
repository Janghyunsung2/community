package com.myproject.community.api.auth.service;

import com.myproject.community.api.account.AccountRepository;
import com.myproject.community.domain.account.Account;
import com.myproject.community.error.CustomException;
import com.myproject.community.error.ErrorCode;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Account account = accountRepository.findByUsername(username).orElseThrow(
            () -> new CustomException(ErrorCode.FORBIDDEN)
        );

        return User.withUsername(account.getUsername())
            .password(account.getPassword())
            .roles(account.getRole().name())
            .build();
    }
}
