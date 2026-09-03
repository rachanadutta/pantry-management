package com.example.pantry.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String refreshToken;
    private String type = "Bearer";
    private String name;
    private String email;
    private String message;
}
