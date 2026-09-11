package com.smartbanking.account.controller;

import com.smartbanking.account.dto.AccountRequest;
import com.smartbanking.account.dto.AccountResponse;
import com.smartbanking.account.service.AccountService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'BANK_EMPLOYEE')")
    public ResponseEntity<AccountResponse> createAccount(
            @Valid @RequestBody AccountRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(accountService.createAccount(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'BANK_EMPLOYEE')")
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {

        return ResponseEntity.ok(
                accountService.getAllAccounts());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN', 'BANK_EMPLOYEE')")
    public ResponseEntity<AccountResponse> getAccount(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                accountService.getAccountById(id));
    }

    @GetMapping("/number/{accountNumber}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN', 'BANK_EMPLOYEE')")
    public ResponseEntity<AccountResponse> getAccountByNumber(
            @PathVariable String accountNumber) {

        return ResponseEntity.ok(
                accountService.getAccountByNumber(accountNumber));
    }

    @PostMapping("/{id}/deposit")
    @PreAuthorize("hasAnyRole('ADMIN', 'BANK_EMPLOYEE')")
    public ResponseEntity<AccountResponse> deposit(
            @PathVariable Long id,
            @RequestParam BigDecimal amount) {

        return ResponseEntity.ok(
                accountService.deposit(id, amount));
    }

    @PostMapping("/{id}/withdraw")
    @PreAuthorize("hasAnyRole('ADMIN', 'BANK_EMPLOYEE')")
    public ResponseEntity<AccountResponse> withdraw(
            @PathVariable Long id,
            @RequestParam BigDecimal amount) {

        return ResponseEntity.ok(
                accountService.withdraw(id, amount));
    }
}