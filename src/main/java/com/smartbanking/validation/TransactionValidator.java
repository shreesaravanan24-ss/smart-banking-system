package com.smartbanking.validation;

import com.smartbanking.transaction.dto.TransferRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransactionValidator {

    public void validateTransfer(TransferRequest request) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Transfer request cannot be null"
            );
        }

        if (request.getSenderAccountNumber() == null
                || request.getSenderAccountNumber().isBlank()) {

            throw new IllegalArgumentException(
                    "Sender account number is required"
            );
        }

        if (request.getReceiverAccountNumber() == null
                || request.getReceiverAccountNumber().isBlank()) {

            throw new IllegalArgumentException(
                    "Receiver account number is required"
            );
        }

        if (request.getSenderAccountNumber()
                .equals(request.getReceiverAccountNumber())) {

            throw new IllegalArgumentException(
                    "Sender and receiver accounts cannot be the same"
            );
        }

        BigDecimal amount = request.getAmount();

        if (amount == null) {

            throw new IllegalArgumentException(
                    "Transaction amount is required"
            );
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "Transaction amount must be greater than zero"
            );
        }

        if (amount.scale() > 2) {

            throw new IllegalArgumentException(
                    "Transaction amount cannot have more than two decimal places"
            );
        }
    }
}
