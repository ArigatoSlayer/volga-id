package com.petrdulnev.authenticationservice.model.dto;

import lombok.Data;

@Data
public class AccountUpdateDto {
    private String firstName;
    private String lastName;
    private String password;
}
