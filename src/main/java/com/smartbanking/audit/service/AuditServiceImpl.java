package com.smartbanking.audit.service;

import com.smartbanking.audit.entity.AuditLog;
import com.smartbanking.audit.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditServiceImpl implements AuditService {

    private final AuditLogRepository auditLogRepository;

    public AuditServiceImpl(
            AuditLogRepository auditLogRepository) {

        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public AuditLog recordAction(
            String action,
            String description,
            String performedBy) {

        AuditLog auditLog = new AuditLog(
                action,
                description,
                performedBy
        );

        return auditLogRepository.save(auditLog);
    }

    @Override
    public List<AuditLog> getLogsByUser(
            String performedBy) {

        return auditLogRepository
                .findByPerformedByOrderByTimestampDesc(
                        performedBy);
    }

    @Override
    public List<AuditLog> getAllLogs() {

        return auditLogRepository.findAll();
    }
}