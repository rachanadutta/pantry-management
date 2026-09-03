package com.example.pantry.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pantry.auth.dto.AuthResponseDTO;
import com.example.pantry.auth.dto.LoginRequestDTO;
import com.example.pantry.auth.dto.LogoutRequestDTO;
import com.example.pantry.auth.dto.RefreshTokenRequestDTO;
import com.example.pantry.auth.dto.RegisterRequestDTO;
import com.example.pantry.auth.service.AuthService;
import com.example.pantry.auth.service.GoogleLoginCodeService;

import jakarta.validation.Valid;

@RestController
public class AuthController {
    private final AuthService authService;
    private final GoogleLoginCodeService googleLoginCodeService;
    public AuthController(AuthService authService, GoogleLoginCodeService googleLoginCodeService) {
        this.authService = authService;
        this.googleLoginCodeService = googleLoginCodeService;
    }
   @PostMapping("/api/auth/register")
    public AuthResponseDTO register(@Valid @RequestBody RegisterRequestDTO request){
        return authService.register(request);
    }
    @PostMapping("/api/auth/login")
    public AuthResponseDTO login(@Valid @RequestBody LoginRequestDTO request){
        return authService.login(request);
    }
    @PostMapping("/api/auth/refresh")
    public AuthResponseDTO refreshToken(@RequestBody RefreshTokenRequestDTO request) {
        return authService.refreshAccessToken(request.getRefreshToken());
       
    }
    @PostMapping("/api/auth/logout")
    public void logout(@Valid @RequestBody LogoutRequestDTO request) {
        authService.logout(request.getRefreshToken());
    }
    @PostMapping("/api/auth/google/exchange")
public AuthResponseDTO exchangeGoogleCode(
        @RequestParam String code) {
            
    return googleLoginCodeService.exchangeCode(code);
}
@GetMapping("/api/auth/google/callback")
public AuthResponseDTO googleCallback(
        @RequestParam String code) {

    return googleLoginCodeService.exchangeCode(code);
}
}
