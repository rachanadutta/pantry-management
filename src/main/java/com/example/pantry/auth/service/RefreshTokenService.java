package com.example.pantry.auth.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.pantry.auth.entity.RefreshToken;
import com.example.pantry.auth.repository.RefreshTokenRepository;
import com.example.pantry.user.entity.User;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final long refreshTokenExpiration;
    public  RefreshTokenService(RefreshTokenRepository refreshTokenRepository,@Value("${jwt.refresh-expiration}") long refreshTokenExpiration){
        this.refreshTokenRepository=refreshTokenRepository;
        this.refreshTokenExpiration=refreshTokenExpiration;
    }

    public RefreshToken createRefreshToken(User user){
        String token = UUID.randomUUID().toString();
        Optional<RefreshToken> existingToken =
        refreshTokenRepository.findByUser(user);
        if (existingToken.isPresent()) {
    refreshTokenRepository.delete(existingToken.get());
}
        Instant expiryDate =
        Instant.now().plusMillis(refreshTokenExpiration);
        RefreshToken refreshToken = new RefreshToken();
refreshToken.setToken(token);
refreshToken.setExpiryDate(expiryDate);
refreshToken.setUser(user);
        return refreshTokenRepository.save(refreshToken);
    }
    public RefreshToken verifyExpiration(RefreshToken token) {

    if (token.getExpiryDate().isBefore(Instant.now())) {
        refreshTokenRepository.delete(token);
        throw new RuntimeException("Refresh token expired");
    }

    return token;
}
public RefreshToken findByToken(String token) {
    return refreshTokenRepository.findByToken(token)
            .orElseThrow(() -> new ResponseStatusException(
        HttpStatus.UNAUTHORIZED,
        "Refresh token not found"
));
}
public void deleteByToken(String token) {
    RefreshToken refreshToken = findByToken(token);
    refreshTokenRepository.delete(refreshToken);
}
}
