package com.petrdulnev.authenticationservice.mapper;

import com.petrdulnev.authenticationservice.model.Account;
import com.petrdulnev.authenticationservice.model.dto.AccountDtoReg;

public class AccountMapper {
    public static Account AccountDtoRegToAccount(AccountDtoReg dtoReg) {
        return Account.builder()
                .firstName(dtoReg.getFirstName())
                .lastName(dtoReg.getLastName())
                .username(dtoReg.getUsername())
                .password(dtoReg.getPassword())
                .build();
    }
}
