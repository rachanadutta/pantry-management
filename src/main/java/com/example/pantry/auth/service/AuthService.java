package com.example.pantry.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.pantry.auth.dto.AuthResponseDTO;
import com.example.pantry.auth.dto.LoginRequestDTO;
import com.example.pantry.auth.dto.RegisterRequestDTO;
import com.example.pantry.auth.entity.RefreshToken;
import com.example.pantry.security.JwtService;
import com.example.pantry.user.entity.User;
import com.example.pantry.user.repository.UserRepository;
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }
   
   public AuthResponseDTO register(RegisterRequestDTO request){
    if(userRepository.existsByEmail(request.getEmail())){
        AuthResponseDTO response = new AuthResponseDTO();
        response.setName(request.getName());
        response.setEmail(request.getEmail());
        response.setMessage("Email already exists"+request.getEmail()+" "+request.getName());
        return response;
    }
    User user = new User();
    
    user.setName(request.getName());
    user.setEmail(request.getEmail());
    String password=request.getPassword();
    String encodedPassword = passwordEncoder.encode(password);
    user.setPasswordHash(encodedPassword);
    
    userRepository.save(user);
    AuthResponseDTO response = new AuthResponseDTO();
    response.setMessage("User registered successfully" );
    response.setName(request.getName());
    response.setEmail(request.getEmail());
    return response;
   }
   public AuthResponseDTO login(LoginRequestDTO request) {

    UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            );

    Authentication authentication =
            authenticationManager.authenticate(authenticationToken);
    User user = userRepository.findByEmail(authentication.getName())
        .orElseThrow(() -> new RuntimeException("User not found"));
    String token = jwtService.generateToken(authentication.getName());
    RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

    AuthResponseDTO response = new AuthResponseDTO();
    response.setMessage("User logged in successfully");
    response.setEmail(authentication.getName());
    response.setToken(token);
    response.setRefreshToken(refreshToken.getToken());
    return response;
}
public AuthResponseDTO refreshAccessToken(String refreshToken) {

    RefreshToken storedToken =
            refreshTokenService.findByToken(refreshToken);

    storedToken =
            refreshTokenService.verifyExpiration(storedToken);

    User user = storedToken.getUser();

    String newAccessToken =
            jwtService.generateToken(user.getEmail());

    AuthResponseDTO response = new AuthResponseDTO();
    response.setToken(newAccessToken);
    response.setRefreshToken(storedToken.getToken());
    response.setEmail(user.getEmail());
    response.setMessage("Access token refreshed successfully");

    return response;
}
public void logout(String refreshToken) {
    refreshTokenService.deleteByToken(refreshToken);
}

}
