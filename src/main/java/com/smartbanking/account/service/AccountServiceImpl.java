package com.smartbanking.account.service;

import com.smartbanking.account.dto.AccountRequest;
import com.smartbanking.account.dto.AccountResponse;
import com.smartbanking.account.entity.Account;
import com.smartbanking.account.repository.AccountRepository;
import com.smartbanking.customer.entity.Customer;
import com.smartbanking.customer.repository.CustomerRepository;
import com.smartbanking.enums.AccountStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    public AccountServiceImpl(
            AccountRepository accountRepository,
            CustomerRepository customerRepository) {

        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public AccountResponse createAccount(AccountRequest request) {

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Customer not found with ID: "
                                        + request.getCustomerId()));

        Account account = new Account();

        String accountNumber;

        do {
            accountNumber = generateAccountNumber();
        } while (accountRepository.existsByAccountNumber(accountNumber));

        account.setAccountNumber(accountNumber);
        account.setAccountType(request.getAccountType());
        account.setStatus(AccountStatus.ACTIVE);
        account.setBalance(BigDecimal.ZERO);
        account.setCustomer(customer);

        return convertToResponse(accountRepository.save(account));
    }

    @Override
    public AccountResponse getAccountById(Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Account not found with ID: " + id));

        return convertToResponse(account);
    }

    @Override
    public AccountResponse getAccountByNumber(String accountNumber) {

        Account account = accountRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Account not found: " + accountNumber));

        return convertToResponse(account);
    }

    @Override
    public List<AccountResponse> getAllAccounts() {

        return accountRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    @Transactional
    public AccountResponse deposit(Long id, BigDecimal amount) {

        validateAmount(amount);

        Account account = getActiveAccount(id);

        account.setBalance(account.getBalance().add(amount));

        return convertToResponse(accountRepository.save(account));
    }

    @Override
    @Transactional
    public AccountResponse withdraw(Long id, BigDecimal amount) {

        validateAmount(amount);

        Account account = getActiveAccount(id);

        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(amount));

        return convertToResponse(accountRepository.save(account));
    }

    private Account getActiveAccount(Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Account not found with ID: " + id));

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalStateException(
                    "Account is not active");
        }

        return account;
    }

    private void validateAmount(BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be greater than zero");
        }
    }

    private String generateAccountNumber() {

        return "SB"
                + System.currentTimeMillis()
                + UUID.randomUUID()
                .toString()
                .substring(0, 4)
                .toUpperCase();
    }

    private AccountResponse convertToResponse(Account account) {

        return new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getAccountType(),
                account.getStatus(),
                account.getBalance(),
                account.getCustomer().getId()
        );
    }
}