package com.smartbanking.audit.controller;

import com.smartbanking.audit.entity.AuditLog;
import com.smartbanking.audit.service.AuditService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
public class AuditController {

    private final AuditService auditService;

    public AuditController(
            AuditService auditService) {

        this.auditService = auditService;
    }

    @GetMapping
    public ResponseEntity<List<AuditLog>> getAllLogs() {

        return ResponseEntity.ok(
                auditService.getAllLogs());
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<List<AuditLog>>
    getUserLogs(@PathVariable String email) {

        return ResponseEntity.ok(
                auditService.getLogsByUser(email));
    }
}