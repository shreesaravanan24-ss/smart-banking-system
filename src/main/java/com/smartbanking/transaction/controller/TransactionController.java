package com.smartbanking.transaction.controller;

import com.smartbanking.transaction.dto.TransactionResponse;
import com.smartbanking.transaction.dto.TransferRequest;
import com.smartbanking.transaction.service.TransactionService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(
            TransactionService transactionService) {

        this.transactionService = transactionService;
    }

    @PostMapping("/transfer")
    @PreAuthorize(
            "hasAnyRole('CUSTOMER', 'BANK_EMPLOYEE', 'ADMIN')"
    )
    public ResponseEntity<TransactionResponse> transfer(
            @Valid @RequestBody TransferRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transactionService.transfer(request));
    }

    @GetMapping("/account/{accountId}")
    @PreAuthorize(
            "hasAnyRole('CUSTOMER', 'BANK_EMPLOYEE', 'ADMIN')"
    )
    public ResponseEntity<List<TransactionResponse>>
    getAccountTransactions(
            @PathVariable Long accountId) {

        return ResponseEntity.ok(
                transactionService
                        .getAccountTransactions(accountId)
        );
    }
}