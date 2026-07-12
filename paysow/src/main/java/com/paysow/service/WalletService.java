package com.paysow.service;

import com.paysow.model.Transaction;

import java.util.List;

public interface WalletService {
    Transaction cashIn(Long userId, double amount);
    Transaction cashOut(Long userId, String destinationPhone, double amount);
    Transaction depositToSavings(Long userId, double amount);
    Transaction withdrawFromSavings(Long userId, double amount);
    List<Transaction> getHistory(Long userId);
    List<Transaction> getRecentHistory(Long userId);
}
