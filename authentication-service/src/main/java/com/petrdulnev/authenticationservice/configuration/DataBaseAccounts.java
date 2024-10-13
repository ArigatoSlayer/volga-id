package com.petrdulnev.authenticationservice.configuration;


import com.petrdulnev.authenticationservice.model.Account;
import com.petrdulnev.authenticationservice.model.Role;
import com.petrdulnev.authenticationservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataBaseAccounts {

    private final AccountRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public void createBaseAccounts() {
        Account admin = new Account(null, "admin", passwordEncoder.encode("admin"),
                "admin", "admin", null, List.of(Role.ADMIN), false);
        Account manager = new Account(null, "manager", passwordEncoder.encode("manager"),
                "manager", "manager", null, List.of(Role.MANAGER), false);
        Account user = new Account(null, "user", passwordEncoder.encode("user"),
                "user", "user", null, List.of(Role.USER), false);
        Account doctor = new Account(null, "doctor", passwordEncoder.encode("doctor"),
                "doctor", "doctor", null, List.of(Role.DOCTOR), false);

        List<Account> accounts = List.of(admin, manager, user, doctor);

        repository.saveAll(accounts);
    }
}
