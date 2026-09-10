package com.smartbanking.transaction.dto;

import com.smartbanking.enums.TransactionStatus;
import com.smartbanking.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponse {

    private Long id;
    private String transactionId;
    private BigDecimal amount;
    private TransactionType type;
    private TransactionStatus status;
    private LocalDateTime createdAt;
    private String accountNumber;

    public TransactionResponse() {
    }

    public TransactionResponse(
            Long id,
            String transactionId,
            BigDecimal amount,
            TransactionType type,
            TransactionStatus status,
            LocalDateTime createdAt,
            String accountNumber) {

        this.id = id;
        this.transactionId = transactionId;
        this.amount = amount;
        this.type = type;
        this.status = status;
        this.createdAt = createdAt;
        this.accountNumber = accountNumber;
    }

    public Long getId() {
        return id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}