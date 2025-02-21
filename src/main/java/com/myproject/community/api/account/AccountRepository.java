package com.myproject.community.api.account;

import com.myproject.community.domain.account.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUsername(String username);

    @Query("select case when count(a) > 0 then true else false end from Account a where a.username =:username")
    boolean existsByUsername(String username);
}
