package com.smartbanking.beneficiary.controller;

import com.smartbanking.beneficiary.dto.BeneficiaryRequest;
import com.smartbanking.beneficiary.dto.BeneficiaryResponse;
import com.smartbanking.beneficiary.service.BeneficiaryService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/beneficiaries")
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;

    public BeneficiaryController(
            BeneficiaryService beneficiaryService) {

        this.beneficiaryService = beneficiaryService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<BeneficiaryResponse> addBeneficiary(
            @Valid @RequestBody BeneficiaryRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        beneficiaryService
                                .addBeneficiary(request)
                );
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<BeneficiaryResponse>>
    getCustomerBeneficiaries(
            @PathVariable Long customerId) {

        return ResponseEntity.ok(
                beneficiaryService
                        .getCustomerBeneficiaries(customerId)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Void> deleteBeneficiary(
            @PathVariable Long id) {

        beneficiaryService.deleteBeneficiary(id);

        return ResponseEntity.noContent().build();
    }
}