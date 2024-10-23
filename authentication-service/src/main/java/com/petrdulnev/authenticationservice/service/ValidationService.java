package com.petrdulnev.authenticationservice.service;

import com.petrdulnev.authenticationservice.configuration.RabbitConfiguration;
import com.petrdulnev.authenticationservice.model.Account;
import com.petrdulnev.authenticationservice.model.Role;
import com.petrdulnev.authenticationservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidationService {

    private final AccountRepository repository;
    private final JwtService jwtService;

    @RabbitListener(queues = RabbitConfiguration.GET_ID_FROM_TOKEN)
    public Long getUserIdFromToken(String token) {
        String username = jwtService.getUsernameFromToken(token);
        Account account = repository.findByUsername(username).orElseThrow();

        return account.getId();
    }

    @RabbitListener(queues = RabbitConfiguration.CHECK_DOCTOR)
    public String checkDoctor(Long id) {
        Account account = repository.findById(id).orElse(null);
        if (account != null && account.getAuthorities().contains(Role.DOCTOR)) {
            return "true";
        } else {
            return "false";
        }
    }

    @RabbitListener(queues = RabbitConfiguration.CHECK_ACCOUNT)
    public Boolean checkAccount(Long id) {
        Account account = repository.findById(id).orElse(null);

        return account != null;
    }

}
