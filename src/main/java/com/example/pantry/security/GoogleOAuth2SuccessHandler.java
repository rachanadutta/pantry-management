package com.example.pantry.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.pantry.auth.entity.GoogleLoginCode;
import com.example.pantry.auth.service.GoogleAuthService;
import com.example.pantry.auth.service.GoogleLoginCodeService;
import com.example.pantry.user.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class GoogleOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final GoogleAuthService googleAuthService;
    private final GoogleLoginCodeService googleLoginCodeService;

    public GoogleOAuth2SuccessHandler(
            GoogleAuthService googleAuthService,
            GoogleLoginCodeService googleLoginCodeService) {

        this.googleAuthService = googleAuthService;
        this.googleLoginCodeService = googleLoginCodeService;
    }


    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        OidcUser oidcUser =
                (OidcUser) authentication.getPrincipal();

        User user =
                googleAuthService.findOrCreateGoogleUser(oidcUser);

        GoogleLoginCode loginCode =
                googleLoginCodeService.createCode(user);

        response.sendRedirect(
                "http://localhost:8080/api/auth/google/callback?code="
                        + loginCode.getCode()
        );
    }
}