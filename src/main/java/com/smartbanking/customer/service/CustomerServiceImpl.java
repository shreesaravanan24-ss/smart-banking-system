package com.smartbanking.customer.service;

import com.smartbanking.customer.dto.CustomerRequest;
import com.smartbanking.customer.dto.CustomerResponse;
import com.smartbanking.customer.entity.Customer;
import com.smartbanking.customer.repository.CustomerRepository;
import com.smartbanking.enums.Role;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerServiceImpl(
            CustomerRepository customerRepository,
            PasswordEncoder passwordEncoder) {

        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {

        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException(
                    "Email already exists");
        }

        if (customerRepository.existsByPhone(request.getPhone())) {
            throw new IllegalArgumentException(
                    "Phone number already exists");
        }

        Customer customer = new Customer(
                request.getFullName(),
                request.getEmail(),
                request.getPhone(),
                passwordEncoder.encode(request.getPassword())
        );

        customer.setRole(Role.CUSTOMER);

        Customer savedCustomer =
                customerRepository.save(customer);

        return convertToResponse(savedCustomer);
    }

    @Override
    public CustomerResponse getCustomerById(Long id) {

        Customer customer = findCustomer(id);

        verifyAccess(customer);

        return convertToResponse(customer);
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        if (!isPrivilegedUser(authentication)) {
            throw new SecurityException(
                    "Only admin or bank employee can view all customers");
        }

        return customerRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public CustomerResponse updateCustomer(
            Long id,
            CustomerRequest request) {

        Customer customer = findCustomer(id);

        verifyAccess(customer);

        if (!customer.getEmail()
                .equalsIgnoreCase(request.getEmail())
                && customerRepository
                .existsByEmail(request.getEmail())) {

            throw new IllegalArgumentException(
                    "Email already exists");
        }

        if (!customer.getPhone()
                .equals(request.getPhone())
                && customerRepository
                .existsByPhone(request.getPhone())) {

            throw new IllegalArgumentException(
                    "Phone number already exists");
        }

        customer.setFullName(request.getFullName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());

        if (request.getPassword() != null
                && !request.getPassword().isBlank()) {

            customer.setPassword(
                    passwordEncoder.encode(
                            request.getPassword()
                    )
            );
        }

        Customer updatedCustomer =
                customerRepository.save(customer);

        return convertToResponse(updatedCustomer);
    }

    @Override
    public void deleteCustomer(Long id) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        if (!isPrivilegedUser(authentication)) {
            throw new SecurityException(
                    "Only admin or bank employee can delete customers");
        }

        if (!customerRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "Customer not found with ID: " + id);
        }

        customerRepository.deleteById(id);
    }

    private Customer findCustomer(Long id) {

        return customerRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Customer not found with ID: " + id));
    }

    private void verifyAccess(Customer customer) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()) {

            throw new SecurityException(
                    "User is not authenticated");
        }

        if (isPrivilegedUser(authentication)) {
            return;
        }

        String loggedInEmail =
                authentication.getName();

        if (customer.getEmail() == null
                || !customer.getEmail()
                .equalsIgnoreCase(loggedInEmail)) {

            throw new SecurityException(
                    "You are not authorized to access this customer data");
        }
    }

    private boolean isPrivilegedUser(
            Authentication authentication) {

        if (authentication == null
                || authentication.getAuthorities() == null) {

            return false;
        }

        return authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        "ROLE_ADMIN".equals(
                                authority.getAuthority())
                                || "ROLE_BANK_EMPLOYEE".equals(
                                authority.getAuthority()));
    }

    private CustomerResponse convertToResponse(
            Customer customer) {

        return new CustomerResponse(
                customer.getId(),
                customer.getFullName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getRole()
        );
    }
}
