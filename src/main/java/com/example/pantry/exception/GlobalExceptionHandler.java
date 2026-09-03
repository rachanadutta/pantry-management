package com.example.pantry.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.example.pantry.auth.dto.AuthResponseDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<AuthResponseDTO> handleBadCredentials(BadCredentialsException e) {
        AuthResponseDTO response = new AuthResponseDTO();
        response.setMessage("Invalid email or password");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
    @ExceptionHandler(ResponseStatusException.class)
public ResponseEntity<AuthResponseDTO> handleResponseStatusException(
        ResponseStatusException e) {

    AuthResponseDTO response = new AuthResponseDTO();
    response.setMessage(e.getReason());

    return ResponseEntity
            .status(e.getStatusCode())
            .body(response);
}

@ExceptionHandler(ResourceNotFoundException.class)
public ResponseEntity<ApiErrorResponse> handleResourceNotFound(
        ResourceNotFoundException e) {

    ApiErrorResponse response = new ApiErrorResponse(e.getMessage());

    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(response);
}
}
