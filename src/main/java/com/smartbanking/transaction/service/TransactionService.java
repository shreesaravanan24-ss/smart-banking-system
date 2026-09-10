package com.smartbanking.transaction.service;

import com.smartbanking.transaction.dto.TransactionResponse;
import com.smartbanking.transaction.dto.TransferRequest;

import java.util.List;

public interface TransactionService {

    TransactionResponse transfer(TransferRequest request);

    List<TransactionResponse> getAccountTransactions(Long accountId);
}