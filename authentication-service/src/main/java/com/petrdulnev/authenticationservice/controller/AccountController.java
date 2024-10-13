package com.petrdulnev.authenticationservice.controller;

import com.petrdulnev.authenticationservice.model.Account;
import com.petrdulnev.authenticationservice.model.dto.AccountDtoIn;
import com.petrdulnev.authenticationservice.model.dto.AccountDtoReg;
import com.petrdulnev.authenticationservice.model.dto.AccountUpdateDto;
import com.petrdulnev.authenticationservice.model.dto.TokenResponse;
import com.petrdulnev.authenticationservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/Authentication")
public class AccountController {

    private final AccountService accountService;

//    Open

    @PostMapping("/SignUp")
    public ResponseEntity<String> signUp(@RequestBody AccountDtoReg account) {
        accountService.signUp(account);
        return ResponseEntity.ok().body("Register successful");
    }

    @PostMapping("/SignIn")
    public TokenResponse signIn(@RequestBody AccountDtoIn account) {
        return accountService.signIn(account);
    }

    @PutMapping("/SignOut")
    public void signOt(@RequestHeader String refreshToken) {
        accountService.signOut(refreshToken);
    }

    @GetMapping("/Validate")
    public String validate(@RequestHeader(name = "Authorization") String token,
                           @RequestHeader String refreshToken) {
        return accountService.validateToken(token, refreshToken);
    }

    @GetMapping("/Refresh")
    public TokenResponse refresh(@RequestHeader String refreshToken) {
        return accountService.refresh(refreshToken);
    }

//    Only Auth

    @GetMapping("/Me")
    public Account me(@RequestHeader(name = "Authorization") String token) {
        return accountService.getMyAccount(token);
    }

    @PutMapping("/Update")
    public Account Update(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody AccountUpdateDto account) {
        return accountService.update(token, account);

    }

    @GetMapping("/Doctors")
    public Page<Account> getDoctors(
            @RequestParam(name = "nameFilter", required = false) String name,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "count", defaultValue = "10") int count
    ) {
        return accountService.getAllDoctors(name, from, count);
    }

    @GetMapping("/Doctors/{id}")
    public Account getByDoctorById(@PathVariable int id) {
        return accountService.getDoctorById(id);
    }

    // Only Admins

    @GetMapping
    public Page<Account> getAll(
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "count", defaultValue = "10") int count,
            @RequestHeader(name = "Authorization") String token
    ) {
        return accountService.getAll(from, count, token);
    }

    @PostMapping
    public Account createByAdmin(@RequestBody Account account,
                                 @RequestHeader(name = "Authorization") String token
    ) {
        return accountService.createByAdmin(account, token);
    }

    @PutMapping("/{id}")
    public Account updateByAdmin(@PathVariable long id,
                                 @RequestBody Account account,
                                 @RequestHeader(name = "Authorization") String token
    ) {
        return accountService.updateByAdmin(id, account, token);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> Delete(@PathVariable long id,
                                         @RequestHeader(name = "Authorization") String token) {
        accountService.deleteAccount(id, token);
        return ResponseEntity.ok().body(null);
    }

}
