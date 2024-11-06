package com.basics.basics.advice;

import com.basics.basics.exceptions.ResourceNotFoundException;
import com.basics.basics.exceptions.TestException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException expection) {
        ApiError apiError = new ApiError(expection.getMessage(), HttpStatus.NOT_FOUND);

        ResponseEntity<ApiError> response = new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);

        return response;
    }

    @ExceptionHandler(TestException.class)
    public ResponseEntity<ApiError> handleTestException(TestException expection) {
        ApiError apiError = new ApiError(expection.getMessage(), HttpStatus.BAD_REQUEST);
        ResponseEntity<ApiError> response = new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
        return response;
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException expection) {
        ApiError apiError = new ApiError(expection.getMessage(), HttpStatus.UNAUTHORIZED);
        ResponseEntity<ApiError> response = new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
        return response;
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiError> handleJwtException(JwtException expection) {
        ApiError apiError = new ApiError(expection.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        ResponseEntity<ApiError> response = new ResponseEntity<>(apiError, HttpStatus.NOT_ACCEPTABLE);
        return response;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDeniedException(AccessDeniedException expection) {
        ApiError apiError = new ApiError(expection.getMessage(), HttpStatus.FORBIDDEN);
        ResponseEntity<ApiError> response = new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
        return response;
    }
}
