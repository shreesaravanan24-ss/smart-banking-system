package com.smartbanking.account.service;

import com.smartbanking.account.dto.AccountRequest;
import com.smartbanking.account.dto.AccountResponse;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    AccountResponse createAccount(AccountRequest request);

    AccountResponse getAccountById(Long id);

    AccountResponse getAccountByNumber(String accountNumber);

    List<AccountResponse> getAllAccounts();

    AccountResponse deposit(Long id, BigDecimal amount);

    AccountResponse withdraw(Long id, BigDecimal amount);
}