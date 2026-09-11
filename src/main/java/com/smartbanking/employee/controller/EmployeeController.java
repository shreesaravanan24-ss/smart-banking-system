package com.smartbanking.employee.controller;

import com.smartbanking.account.dto.AccountResponse;
import com.smartbanking.account.service.AccountService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@PreAuthorize("hasAnyRole('ADMIN', 'BANK_EMPLOYEE')")
public class EmployeeController {

    private final AccountService accountService;

    public EmployeeController(
            AccountService accountService) {

        this.accountService = accountService;
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {

        return ResponseEntity.ok(
                accountService.getAllAccounts()
        );
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<AccountResponse> getAccount(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                accountService.getAccountById(id)
        );
    }
}