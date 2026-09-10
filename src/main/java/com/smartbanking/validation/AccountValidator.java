package com.smartbanking.validation;

import com.smartbanking.account.entity.Account;
import com.smartbanking.enums.AccountStatus;

import java.math.BigDecimal;

public final class AccountValidator {

    private AccountValidator() {
    }

    public static void validateAmount(BigDecimal amount) {

        if (amount == null) {
            throw new IllegalArgumentException(
                    "Amount is required"
            );
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be greater than zero"
            );
        }
    }

    public static void validateActiveAccount(Account account) {

        if (account == null) {
            throw new IllegalArgumentException(
                    "Account is required"
            );
        }

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalStateException(
                    "Account is not active"
            );
        }
    }

    public static void validateSufficientBalance(
            Account account,
            BigDecimal amount) {

        if (account.getBalance().compareTo(amount) < 0) {
            throw new com.smartbanking.exception
                    .InsufficientBalanceException(
                    "Insufficient balance"
            );
        }
    }
}