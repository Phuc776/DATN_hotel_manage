package com.do_issac.hotel_manage.controller;

import com.do_issac.hotel_manage.dto.LoginRequest;
import com.do_issac.hotel_manage.dto.LoginResponse;
import com.do_issac.hotel_manage.dto.RegisterRequest;
import com.do_issac.hotel_manage.dto.RegisterResponse;
import com.do_issac.hotel_manage.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest req) {
        return authService.login(req);
    }

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest req) {
        return authService.register(req);
    }
}
