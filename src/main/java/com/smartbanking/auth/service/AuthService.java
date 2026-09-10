package com.smartbanking.auth.service;

import com.smartbanking.auth.dto.AuthResponse;
import com.smartbanking.auth.dto.LoginRequest;

public interface AuthService {

    AuthResponse login(LoginRequest request);
}