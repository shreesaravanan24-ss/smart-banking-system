package com.smartbanking.beneficiary.service;

import com.smartbanking.beneficiary.dto.BeneficiaryRequest;
import com.smartbanking.beneficiary.dto.BeneficiaryResponse;
import com.smartbanking.beneficiary.entity.Beneficiary;
import com.smartbanking.beneficiary.repository.BeneficiaryRepository;
import com.smartbanking.customer.entity.Customer;
import com.smartbanking.customer.repository.CustomerRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BeneficiaryServiceImpl
        implements BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;
    private final CustomerRepository customerRepository;

    public BeneficiaryServiceImpl(
            BeneficiaryRepository beneficiaryRepository,
            CustomerRepository customerRepository) {

        this.beneficiaryRepository = beneficiaryRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public BeneficiaryResponse addBeneficiary(
            BeneficiaryRequest request) {

        Customer customer = customerRepository
                .findById(request.getCustomerId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Customer not found with ID: "
                                        + request.getCustomerId()));

        verifyCustomerOwnership(customer);

        if (beneficiaryRepository
                .existsByCustomerIdAndAccountNumber(
                        customer.getId(),
                        request.getAccountNumber())) {

            throw new IllegalArgumentException(
                    "Beneficiary already exists");
        }

        Beneficiary beneficiary = new Beneficiary();

        beneficiary.setName(request.getName());
        beneficiary.setAccountNumber(
                request.getAccountNumber());
        beneficiary.setBankName(
                request.getBankName());
        beneficiary.setIfscCode(
                request.getIfscCode());
        beneficiary.setCustomer(customer);

        Beneficiary saved =
                beneficiaryRepository.save(beneficiary);

        return convertToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BeneficiaryResponse>
    getCustomerBeneficiaries(Long customerId) {

        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Customer not found with ID: "
                                        + customerId));

        verifyCustomerOwnership(customer);

        return beneficiaryRepository
                .findByCustomerId(customerId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteBeneficiary(Long id) {

        Beneficiary beneficiary =
                beneficiaryRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Beneficiary not found with ID: "
                                                + id));

        verifyCustomerOwnership(
                beneficiary.getCustomer());

        beneficiaryRepository.delete(beneficiary);
    }

    private void verifyCustomerOwnership(
            Customer customer) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()) {

            throw new SecurityException(
                    "User is not authenticated");
        }

        String loggedInEmail =
                authentication.getName();

        if (customer == null
                || customer.getEmail() == null
                || !customer.getEmail()
                .equalsIgnoreCase(loggedInEmail)) {

            throw new SecurityException(
                    "You are not authorized to access this customer data");
        }
    }

    private BeneficiaryResponse convertToResponse(
            Beneficiary beneficiary) {

        return new BeneficiaryResponse(
                beneficiary.getId(),
                beneficiary.getName(),
                beneficiary.getAccountNumber(),
                beneficiary.getBankName(),
                beneficiary.getIfscCode(),
                beneficiary.getCustomer().getId()
        );
    }
}