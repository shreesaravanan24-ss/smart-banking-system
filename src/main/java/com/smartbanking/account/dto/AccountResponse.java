package com.smartbanking.account.dto;

import com.smartbanking.enums.AccountStatus;
import com.smartbanking.enums.AccountType;

import java.math.BigDecimal;

public class AccountResponse {

    private Long id;
    private String accountNumber;
    private AccountType accountType;
    private AccountStatus status;
    private BigDecimal balance;
    private Long customerId;

    public AccountResponse() {
    }

    public AccountResponse(Long id, String accountNumber,
                           AccountType accountType,
                           AccountStatus status,
                           BigDecimal balance,
                           Long customerId) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.status = status;
        this.balance = balance;
        this.customerId = customerId;
    }

    public Long getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Long getCustomerId() {
        return customerId;
    }
}