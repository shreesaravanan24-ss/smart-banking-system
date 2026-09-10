package com.smartbanking.admin.controller;

import com.smartbanking.account.dto.AccountResponse;
import com.smartbanking.admin.dto.AccountStatusRequest;
import com.smartbanking.admin.service.AdminService;
import com.smartbanking.customer.dto.CustomerResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/customers")
    public ResponseEntity<List<CustomerResponse>>
    getAllCustomers() {

        return ResponseEntity.ok(
                adminService.getAllCustomers());
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountResponse>>
    getAllAccounts() {

        return ResponseEntity.ok(
                adminService.getAllAccounts());
    }

    @PutMapping("/accounts/{id}/status")
    public ResponseEntity<AccountResponse>
    updateAccountStatus(
            @PathVariable Long id,
            @Valid @RequestBody
            AccountStatusRequest request) {

        return ResponseEntity.ok(
                adminService.updateAccountStatus(
                        id,
                        request.getStatus().name()));
    }
}