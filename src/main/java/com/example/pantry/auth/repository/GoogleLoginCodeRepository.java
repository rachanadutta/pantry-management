package com.example.pantry.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pantry.auth.entity.GoogleLoginCode;

public interface GoogleLoginCodeRepository
        extends JpaRepository<GoogleLoginCode, Long> {

    Optional<GoogleLoginCode> findByCode(String code);
    void deleteByCode(String code);
}