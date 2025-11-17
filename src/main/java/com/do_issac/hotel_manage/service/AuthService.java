package com.do_issac.hotel_manage.service;

import com.do_issac.hotel_manage.dto.LoginRequest;
import com.do_issac.hotel_manage.dto.LoginResponse;
import com.do_issac.hotel_manage.dto.RegisterRequest;
import com.do_issac.hotel_manage.dto.RegisterResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    RegisterResponse register(RegisterRequest request);
}
