package com.smartbanking.admin.service;

import com.smartbanking.account.dto.AccountResponse;
import com.smartbanking.account.entity.Account;
import com.smartbanking.account.repository.AccountRepository;
import com.smartbanking.customer.dto.CustomerResponse;
import com.smartbanking.customer.entity.Customer;
import com.smartbanking.customer.repository.CustomerRepository;
import com.smartbanking.enums.AccountStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;

    public AdminServiceImpl(
            CustomerRepository customerRepository,
            AccountRepository accountRepository) {

        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {

        return customerRepository.findAll()
                .stream()
                .map(this::convertCustomer)
                .toList();
    }

    @Override
    public List<AccountResponse> getAllAccounts() {

        return accountRepository.findAll()
                .stream()
                .map(this::convertAccount)
                .toList();
    }

    @Override
    @Transactional
    public AccountResponse updateAccountStatus(
            Long accountId,
            String status) {

        Account account = accountRepository
                .findById(accountId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Account not found with ID: "
                                        + accountId));

        AccountStatus newStatus;

        try {
            newStatus =
                    AccountStatus.valueOf(
                            status.toUpperCase());
        } catch (IllegalArgumentException exception) {

            throw new IllegalArgumentException(
                    "Invalid account status: " + status);
        }

        account.setStatus(newStatus);

        return convertAccount(
                accountRepository.save(account));
    }

    private CustomerResponse convertCustomer(
            Customer customer) {

        return new CustomerResponse(
                customer.getId(),
                customer.getFullName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getRole()
        );
    }

    private AccountResponse convertAccount(
            Account account) {

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