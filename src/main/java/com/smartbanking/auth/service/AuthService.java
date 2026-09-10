package com.smartbanking.auth.service;

import com.smartbanking.auth.dto.AuthResponse;
import com.smartbanking.auth.dto.LoginRequest;
import com.smartbanking.auth.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}