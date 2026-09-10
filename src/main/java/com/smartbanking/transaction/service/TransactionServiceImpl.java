package com.smartbanking.transaction.service;

import com.smartbanking.account.entity.Account;
import com.smartbanking.account.repository.AccountRepository;
import com.smartbanking.enums.AccountStatus;
import com.smartbanking.enums.TransactionStatus;
import com.smartbanking.enums.TransactionType;
import com.smartbanking.transaction.dto.TransactionResponse;
import com.smartbanking.transaction.dto.TransferRequest;
import com.smartbanking.transaction.entity.Transaction;
import com.smartbanking.transaction.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository) {

        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional
    public TransactionResponse transfer(TransferRequest request) {

        if (request.getSenderAccountNumber()
                .equals(request.getReceiverAccountNumber())) {

            throw new IllegalArgumentException(
                    "Sender and receiver accounts cannot be the same");
        }

        if (request.getAmount() == null ||
                request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "Transfer amount must be greater than zero");
        }

        Account sender = accountRepository
                .findByAccountNumber(request.getSenderAccountNumber())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Sender account not found"));

        Account receiver = accountRepository
                .findByAccountNumber(request.getReceiverAccountNumber())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Receiver account not found"));

        validateAccount(sender);
        validateAccount(receiver);

        if (sender.getBalance()
                .compareTo(request.getAmount()) < 0) {

            throw new IllegalArgumentException(
                    "Insufficient balance");
        }

        sender.setBalance(
                sender.getBalance()
                        .subtract(request.getAmount()));

        receiver.setBalance(
                receiver.getBalance()
                        .add(request.getAmount()));

        accountRepository.save(sender);
        accountRepository.save(receiver);

        Transaction transaction = new Transaction();

        transaction.setTransactionId(generateTransactionId());
        transaction.setAmount(request.getAmount());
        transaction.setType(TransactionType.TRANSFER);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setAccount(sender);

        Transaction savedTransaction =
                transactionRepository.save(transaction);

        return convertToResponse(savedTransaction);
    }

    @Override
    public List<TransactionResponse> getAccountTransactions(Long accountId) {

        return transactionRepository
                .findByAccountIdOrderByCreatedAtDesc(accountId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    private void validateAccount(Account account) {

        if (account.getStatus() != AccountStatus.ACTIVE) {

            throw new IllegalStateException(
                    "Account " + account.getAccountNumber()
                            + " is not active");
        }
    }

    private String generateTransactionId() {

        return "TXN-"
                + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12)
                .toUpperCase();
    }

    private TransactionResponse convertToResponse(
            Transaction transaction) {

        return new TransactionResponse(
                transaction.getId(),
                transaction.getTransactionId(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getStatus(),
                transaction.getCreatedAt(),
                transaction.getAccount().getAccountNumber()
        );
    }
}