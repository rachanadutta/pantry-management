package com.example.pantry.auth.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.pantry.auth.dto.AuthResponseDTO;
import com.example.pantry.auth.entity.GoogleLoginCode;
import com.example.pantry.auth.entity.RefreshToken;
import com.example.pantry.auth.repository.GoogleLoginCodeRepository;
import com.example.pantry.security.JwtService;
import com.example.pantry.user.entity.User;

@Service
public class GoogleLoginCodeService {

    private final GoogleLoginCodeRepository repository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public GoogleLoginCodeService(
            GoogleLoginCodeRepository repository,
            JwtService jwtService,
            RefreshTokenService refreshTokenService) {

        this.repository = repository;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    public GoogleLoginCode createCode(User user) {

        GoogleLoginCode loginCode = new GoogleLoginCode();

        loginCode.setCode(UUID.randomUUID().toString());
        loginCode.setUser(user);
        loginCode.setExpiryDate(
                Instant.now().plusSeconds(120)
        );

        return repository.save(loginCode);
    }

    public AuthResponseDTO exchangeCode(String code) {

        GoogleLoginCode loginCode =
                repository.findByCode(code)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Invalid Google login code"));

        if (loginCode.getExpiryDate().isBefore(Instant.now())) {

            repository.delete(loginCode);

            throw new RuntimeException(
                    "Google login code expired");
        }

        User user = loginCode.getUser();

        // Make the code single-use
        repository.delete(loginCode);

        String accessToken =
                jwtService.generateToken(user.getEmail());

        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user);

        AuthResponseDTO response = new AuthResponseDTO();

        response.setMessage("Google login successful");
        response.setEmail(user.getEmail());
        response.setName(user.getName());
        response.setToken(accessToken);
        response.setRefreshToken(refreshToken.getToken());

        return response;
    }
}