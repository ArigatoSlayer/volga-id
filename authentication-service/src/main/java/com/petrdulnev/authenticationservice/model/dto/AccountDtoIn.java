package com.petrdulnev.authenticationservice.model.dto;

import lombok.Data;

@Data
public class AccountDtoIn {
    private String username;
    private String password;
}
