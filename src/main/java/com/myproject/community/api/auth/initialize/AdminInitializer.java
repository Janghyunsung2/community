package com.myproject.community.api.auth.initialize;

import com.myproject.community.api.account.AccountRepository;
import com.myproject.community.api.member.repository.MemberRepository;
import com.myproject.community.api.member.repository.MemberStatusRepository;
import com.myproject.community.domain.account.Account;
import com.myproject.community.domain.account.Role;
import com.myproject.community.domain.member.Member;
import com.myproject.community.domain.member.MemberStatus;
import com.myproject.community.domain.member.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberStatusRepository memberStatusRepository;


    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if(accountRepository.findByUsername("admin").isEmpty()) {
            MemberStatus memberStatus = MemberStatus.builder().status(Status.ACTIVE).build();
            memberStatusRepository.save(memberStatus);
            Member member = Member.builder().nickName("관리자").memberStatus(memberStatus).build();

            Account admin = Account.builder().username("admin")
                .password(passwordEncoder.encode("1234")).role(Role.ADMIN).member(member).build();
            memberRepository.save(member);
            accountRepository.save(admin);
        }
    }
}
