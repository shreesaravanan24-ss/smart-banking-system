package com.smartbanking.audit.service;

import com.smartbanking.audit.entity.AuditLog;

import java.util.List;

public interface AuditService {

    AuditLog recordAction(
            String action,
            String description,
            String performedBy);

    List<AuditLog> getLogsByUser(
            String performedBy);

    List<AuditLog> getAllLogs();
}