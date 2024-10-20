package com.petrdulnev.authenticationservice.service;

import com.petrdulnev.authenticationservice.mapper.AccountMapper;
import com.petrdulnev.authenticationservice.model.Account;
import com.petrdulnev.authenticationservice.model.Role;
import com.petrdulnev.authenticationservice.model.dto.AccountDtoIn;
import com.petrdulnev.authenticationservice.model.dto.AccountDtoReg;
import com.petrdulnev.authenticationservice.model.dto.AccountUpdateDto;
import com.petrdulnev.authenticationservice.model.dto.TokenResponse;
import com.petrdulnev.authenticationservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public void signUp(AccountDtoReg accountDtoReg) {
        Account account = AccountMapper.AccountDtoRegToAccount(accountDtoReg);

        account.setPassword(passwordEncoder.encode(accountDtoReg.getPassword()));
        account.setDeleted(false);

        account.setAuthorities(new ArrayList<>());
        account.getAuthorities().add(Role.USER);

        accountRepository.save(account);
    }

    @Transactional
    public TokenResponse signIn(AccountDtoIn accountDtoIn) {
        Account account = accountRepository.findByUsername(accountDtoIn.getUsername()).orElseThrow(
                () -> new RuntimeException("Account not found")
        );

        if (passwordEncoder.matches(accountDtoIn.getPassword(), account.getPassword())) {
            String token = jwtService.generateToken(account);
            String refreshToken = jwtService.generateRefreshToken();

            account.setRefreshToken(refreshToken);
            accountRepository.save(account);

            return new TokenResponse(token, refreshToken);
        }

        throw new RuntimeException("Wrong password");
    }

    @Transactional
    public void signOut(String refreshToken) {
        Account account = accountRepository.findByRefreshToken(refreshToken).orElseThrow();
        account.setRefreshToken(null);
        accountRepository.save(account);
    }

    public String validateToken(String token, String refreshToken) {
        StringBuilder builder = new StringBuilder();
        if (jwtService.isValidToken(token)) {
            builder.append("Access token valid! \n");
        } else {
            builder.append("Access token invalid! \n");
        }

        if (jwtService.isValidToken(refreshToken)) {
            builder.append("Refresh token valid!");
        } else {
            builder.append("Refresh token invalid!");
        }

        return builder.toString();
    }

    public TokenResponse refresh(String refreshToken) {
        Account account = accountRepository.findByRefreshToken(refreshToken).orElseThrow();

        String newRefreshToken = jwtService.generateRefreshToken();
        account.setRefreshToken(newRefreshToken);
        accountRepository.save(account);

        String accessToken = jwtService.generateToken(account);

        return new TokenResponse(accessToken, newRefreshToken);
    }

    public Page<Account> getAll(int from, int count, String token) {
        isAdmin(token);
        return accountRepository.findAll(PageRequest.of(from, count));
    }

    public Account createByAdmin(Account account, String token) {
        isAdmin(token);
        return accountRepository.save(account);
    }

    public Account updateByAdmin(long id, Account account, String token) {
        isAdmin(token);

        Account oldAccount = getAccountById(id);

//        Дописать логику обновления
        return null;
    }

    @Transactional
    public void deleteAccount(long id, String token) {
        isAdmin(token);

        Account account = getAccountById(id);
        account.setDeleted(true);
        accountRepository.save(account);
    }

    public Account getDoctorById(int id) {
        Account account = getAccountById(id);
        if (account.getAuthorities().contains(Role.DOCTOR)) {
            return account;
        } else {
            throw new RuntimeException("Doctor not found");
        }
    }

    public Account getMyAccount(String token) {
        String accountUsername = jwtService.getUsernameFromToken(token);
        return getAccountByUsername(accountUsername);
    }

    @Transactional
    public Account update(String token, AccountUpdateDto account) {
        Account oldAccount = getAccountByUsername(jwtService.getUsernameFromToken(token));

        if (account.getFirstName() != null) {
            oldAccount.setFirstName(account.getFirstName());
        } else if (account.getLastName() != null) {
            oldAccount.setLastName(account.getLastName());
        } else if (account.getPassword() != null) {
            String newPassword = passwordEncoder.encode(account.getPassword());
            oldAccount.setPassword(newPassword);
        }

        return accountRepository.save(oldAccount);
    }

    @SneakyThrows
    private Account getAccountByUsername(String username) {
        return accountRepository.findByUsername(username).orElseThrow(
                () -> new AccountNotFoundException(
                        String.format("Can't find account with id = %username", username)
                )
        );
    }

    @SneakyThrows
    private Account getAccountById(long id) {
        return accountRepository.findById(id).orElseThrow(
                () -> new AccountNotFoundException(
                        String.format("Can't find account with id = %sid", id)
                )
        );
    }

    public Page<Account> getAllDoctors(String name, int from, int count) {
        return accountRepository
                .getAccountByAuthoritiesAndFirstNameContainingIgnoreCase(List.of(Role.DOCTOR), name, PageRequest.of(from, count));
    }

    private void isAdmin(String token) {
        if (jwtService.getRolesFromToken(token).contains(Role.ADMIN.toString())) {
            return;
        } else {
            throw new RuntimeException("Admin only");
        }
    }

}
