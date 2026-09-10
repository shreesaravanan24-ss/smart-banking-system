package com.smartbanking.customer.service;

import com.smartbanking.customer.dto.CustomerRequest;
import com.smartbanking.customer.dto.CustomerResponse;
import com.smartbanking.customer.entity.Customer;
import com.smartbanking.customer.repository.CustomerRepository;
import com.smartbanking.enums.Role;
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

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Customer not found with ID: " + id));

        return convertToResponse(customer);
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {

        return customerRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public CustomerResponse updateCustomer(
            Long id,
            CustomerRequest request) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Customer not found with ID: " + id));

        if (!customer.getEmail().equals(request.getEmail())
                && customerRepository.existsByEmail(request.getEmail())) {

            throw new IllegalArgumentException(
                    "Email already exists");
        }

        if (!customer.getPhone().equals(request.getPhone())
                && customerRepository.existsByPhone(request.getPhone())) {

            throw new IllegalArgumentException(
                    "Phone number already exists");
        }

        customer.setFullName(request.getFullName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());

        /*
         * Only change the password when a new password
         * has actually been supplied.
         */
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

        if (!customerRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "Customer not found with ID: " + id);
        }

        customerRepository.deleteById(id);
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