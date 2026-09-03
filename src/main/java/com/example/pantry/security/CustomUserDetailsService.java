package com.example.pantry.security;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.pantry.user.entity.User;
import com.example.pantry.user.repository.UserRepository;
@Service
public class CustomUserDetailsService implements UserDetailsService{
    private final UserRepository userRepository;
        public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
        }
    @Override
    public UserDetails loadUserByUsername(String email){
        User user= userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User.withUsername(user.getEmail()).password(user.getPasswordHash()).build();
}
}
