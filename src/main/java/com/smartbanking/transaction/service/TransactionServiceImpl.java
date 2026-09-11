package com.smartbanking.transaction.service;

import com.smartbanking.account.entity.Account;
import com.smartbanking.account.repository.AccountRepository;
import com.smartbanking.audit.service.AuditService;
import com.smartbanking.enums.AccountStatus;
import com.smartbanking.enums.TransactionStatus;
import com.smartbanking.enums.TransactionType;
import com.smartbanking.transaction.dto.TransactionResponse;
import com.smartbanking.transaction.dto.TransferRequest;
import com.smartbanking.transaction.entity.Transaction;
import com.smartbanking.transaction.repository.TransactionRepository;
import com.smartbanking.validation.TransactionValidator;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionValidator transactionValidator;
    private final AuditService auditService;

    public TransactionServiceImpl(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository,
            TransactionValidator transactionValidator,
            AuditService auditService) {

        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.transactionValidator = transactionValidator;
        this.auditService = auditService;
    }

    @Override
    @Transactional
    public TransactionResponse transfer(
            TransferRequest request) {

        transactionValidator.validateTransfer(request);

        Account sender = accountRepository
                .findByAccountNumber(
                        request.getSenderAccountNumber())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Sender account not found"));

        Account receiver = accountRepository
                .findByAccountNumber(
                        request.getReceiverAccountNumber())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Receiver account not found"));

        validateAccount(sender);
        validateAccount(receiver);

        verifySenderOwnership(sender);

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

        transaction.setTransactionId(
                generateTransactionId());

        transaction.setAmount(
                request.getAmount());

        transaction.setType(
                TransactionType.TRANSFER);

        transaction.setStatus(
                TransactionStatus.SUCCESS);

        transaction.setAccount(sender);

        Transaction savedTransaction =
                transactionRepository.save(transaction);

        recordAudit(
                "MONEY_TRANSFER",
                "Transferred "
                        + request.getAmount()
                        + " from account "
                        + sender.getAccountNumber()
                        + " to account "
                        + receiver.getAccountNumber()
        );

        return convertToResponse(savedTransaction);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> getAccountTransactions(
            Long accountId) {

        Account account = accountRepository
                .findById(accountId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Account not found"));

        verifySenderOwnership(account);

        return transactionRepository
                .findByAccountIdOrderByCreatedAtDesc(accountId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    private void validateAccount(
            Account account) {

        if (account.getStatus()
                != AccountStatus.ACTIVE) {

            throw new IllegalStateException(
                    "Account "
                            + account.getAccountNumber()
                            + " is not active");
        }
    }

    private void verifySenderOwnership(
            Account account) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()) {

            throw new SecurityException(
                    "User is not authenticated");
        }

        String loggedInEmail =
                authentication.getName();

        if (account.getCustomer() == null
                || account.getCustomer().getEmail() == null
                || !account.getCustomer()
                .getEmail()
                .equalsIgnoreCase(loggedInEmail)) {

            throw new SecurityException(
                    "You are not authorized to access this account");
        }
    }

    private void recordAudit(
            String action,
            String description) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String performedBy = "SYSTEM";

        if (authentication != null
                && authentication.isAuthenticated()) {

            performedBy = authentication.getName();
        }

        auditService.recordAction(
                action,
                description,
                performedBy
        );
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
                transaction.getAccount()
                        .getAccountNumber()
        );
    }
}