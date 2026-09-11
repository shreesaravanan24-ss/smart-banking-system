package com.smartbanking.auth.service;

import com.smartbanking.auth.dto.AuthResponse;
import com.smartbanking.auth.dto.LoginRequest;
import com.smartbanking.auth.dto.RegisterRequest;
import com.smartbanking.customer.entity.Customer;
import com.smartbanking.customer.repository.CustomerRepository;
import com.smartbanking.enums.Role;
import com.smartbanking.security.JwtService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            CustomerRepository customerRepository,
            PasswordEncoder passwordEncoder) {

        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {

        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException(
                    "Email already exists"
            );
        }

        if (customerRepository.existsByPhone(request.getPhone())) {
            throw new IllegalArgumentException(
                    "Phone number already exists"
            );
        }

        Customer customer = new Customer();

        customer.setFullName(request.getFullName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());

        customer.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        // Every user registering through the public API
        // is created as a CUSTOMER.
        customer.setRole(Role.CUSTOMER);

        Customer savedCustomer =
                customerRepository.save(customer);

        String token =
                jwtService.generateToken(
                        savedCustomer.getEmail()
                );

        return new AuthResponse(
                token,
                "Bearer",
                savedCustomer.getEmail()
        );
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String token =
                jwtService.generateToken(
                        request.getEmail()
                );

        return new AuthResponse(
                token,
                "Bearer",
                request.getEmail()
        );
    }
}