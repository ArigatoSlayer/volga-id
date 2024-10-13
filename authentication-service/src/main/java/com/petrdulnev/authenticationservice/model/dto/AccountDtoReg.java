package com.petrdulnev.authenticationservice.model.dto;

import lombok.Data;

@Data
public class AccountDtoReg {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
}
