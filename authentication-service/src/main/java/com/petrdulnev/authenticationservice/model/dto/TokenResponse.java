package com.petrdulnev.authenticationservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TokenResponse {
    private String token;
    private String refreshToken;
}
