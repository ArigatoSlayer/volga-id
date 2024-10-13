package com.petrdulnev.authenticationservice.repository;

import com.petrdulnev.authenticationservice.model.Account;
import com.petrdulnev.authenticationservice.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
//    Page<Account> getAccounts(Pageable pageable);

    Page<Account> getAccountByAuthoritiesAndFirstNameContainingIgnoreCase(List<Role> authorities, String firstName, Pageable pageable);
    Optional<Account> findByUsername(String username);
    Optional<Account> findByRefreshToken(String refreshToken);
}
