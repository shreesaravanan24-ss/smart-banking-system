package com.smartbanking.beneficiary.service;

import com.smartbanking.beneficiary.dto.BeneficiaryRequest;
import com.smartbanking.beneficiary.dto.BeneficiaryResponse;
import com.smartbanking.beneficiary.entity.Beneficiary;
import com.smartbanking.beneficiary.repository.BeneficiaryRepository;
import com.smartbanking.customer.entity.Customer;
import com.smartbanking.customer.repository.CustomerRepository;
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

        if (beneficiaryRepository
                .existsByCustomerIdAndAccountNumber(
                        request.getCustomerId(),
                        request.getAccountNumber())) {

            throw new IllegalArgumentException(
                    "Beneficiary already exists");
        }

        Beneficiary beneficiary = new Beneficiary();

        beneficiary.setName(request.getName());
        beneficiary.setAccountNumber(
                request.getAccountNumber());
        beneficiary.setBankName(request.getBankName());
        beneficiary.setIfscCode(request.getIfscCode());
        beneficiary.setCustomer(customer);

        Beneficiary saved =
                beneficiaryRepository.save(beneficiary);

        return convertToResponse(saved);
    }

    @Override
    public List<BeneficiaryResponse>
    getCustomerBeneficiaries(Long customerId) {

        if (!customerRepository.existsById(customerId)) {
            throw new IllegalArgumentException(
                    "Customer not found with ID: "
                            + customerId);
        }

        return beneficiaryRepository
                .findByCustomerId(customerId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteBeneficiary(Long id) {

        if (!beneficiaryRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "Beneficiary not found with ID: " + id);
        }

        beneficiaryRepository.deleteById(id);
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