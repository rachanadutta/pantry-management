package com.example.pantry.auth.service;

import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import com.example.pantry.user.entity.User;
import com.example.pantry.user.repository.UserRepository;
@Service
public class GoogleAuthService {
    private final UserRepository userRepository;


    public GoogleAuthService(UserRepository userRepository) {
        this.userRepository = userRepository;


    }

    public User findOrCreateGoogleUser(OidcUser oidcUser) {

    String email = oidcUser.getAttribute("email");
    String name = oidcUser.getAttribute("name");
    String googleId = oidcUser.getSubject();

    return userRepository.findByGoogleId(googleId)
            .orElseGet(() -> {

                User existingUser =
                        userRepository.findByEmail(email)
                                .orElse(null);

                if (existingUser != null) {
                    existingUser.setGoogleId(googleId);
                    return userRepository.save(existingUser);
                }

                User newUser = new User();
                newUser.setName(name);
                newUser.setEmail(email);
                newUser.setGoogleId(googleId);
                newUser.setPasswordHash(null);

                return userRepository.save(newUser);
            });
}
}
