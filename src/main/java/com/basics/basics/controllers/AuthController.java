package com.basics.basics.controllers;

import com.basics.basics.dto.LoginDto;
import com.basics.basics.dto.LoginResponseDto;
import com.basics.basics.dto.SignUpDto;
import com.basics.basics.dto.UserDto;
import com.basics.basics.services.AuthService;
import com.basics.basics.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignUpDto signUpDto) {
        UserDto userDto= userService.signUp(signUpDto);

        return ResponseEntity.ok(userDto);

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto, HttpServletResponse response) {
        LoginResponseDto loginResponseDto = authService.login(loginDto);

        Cookie cookie = new Cookie("refreshToken", loginResponseDto.getRefreshToken());
        cookie.setHttpOnly(true);

        response.addCookie(cookie);

        return ResponseEntity.ok(loginResponseDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();

        String refreshToken = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("refreshToken")).findFirst().map(cookie -> cookie.getValue()).orElseThrow(()-> new AuthorizationServiceException("Refresh token not found inside the cookies"));

        LoginResponseDto loginResponseDto = authService.refreshToken(refreshToken);

        return ResponseEntity.ok(loginResponseDto);
    }



}
