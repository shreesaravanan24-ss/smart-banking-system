package com.smartbanking.admin.service;

import com.smartbanking.account.dto.AccountResponse;
import com.smartbanking.customer.dto.CustomerResponse;

import java.util.List;

public interface AdminService {

    List<CustomerResponse> getAllCustomers();

    List<AccountResponse> getAllAccounts();

    AccountResponse updateAccountStatus(
            Long accountId,
            String status);
}