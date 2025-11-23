package com.do_issac.hotel_manage.service;

import com.do_issac.hotel_manage.dto.request.LoginRequest;
import com.do_issac.hotel_manage.dto.response.LoginResponse;
import com.do_issac.hotel_manage.dto.request.RegisterRequest;
import com.do_issac.hotel_manage.dto.response.RegisterResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    RegisterResponse register(RegisterRequest request);
}
