package com.smartbanking.admin.dto;

import com.smartbanking.enums.AccountStatus;
import jakarta.validation.constraints.NotNull;

public class AccountStatusRequest {

    @NotNull(message = "Account status is required")
    private AccountStatus status;

    public AccountStatusRequest() {
    }

    public AccountStatusRequest(AccountStatus status) {
        this.status = status;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }
}