package com.paysow.service.impl;

import com.paysow.exception.InsufficientBalanceException;
import com.paysow.exception.UserNotFoundException;
import com.paysow.model.*;
import com.paysow.repository.TransactionRepository;
import com.paysow.repository.UserRepository;
import com.paysow.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WalletServiceImpl implements WalletService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public WalletServiceImpl(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional
    public Transaction cashIn(Long userId, double amount) {
        User user = getUser(userId);
        user.credit(amount);
        userRepository.save(user);

        Transaction txn = new CashInTransaction(user, amount, user.getWalletBalance(), "Top-up");
        return transactionRepository.save(txn);
    }

    @Override
    @Transactional
    public Transaction cashOut(Long userId, String destinationPhone, double amount) {
        User sender = getUser(userId);

        if (sender.getWalletBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient wallet balance for this cash out.");
        }
        if (sender.getPhoneNumber().equals(destinationPhone)) {
            throw new IllegalArgumentException("You cannot cash out to your own number.");
        }

        User receiver = userRepository.findByPhoneNumber(destinationPhone)
                .orElseThrow(() -> new UserNotFoundException("No Paysow account is linked to that phone number."));

        sender.debit(amount);
        receiver.credit(amount);
        userRepository.save(sender);
        userRepository.save(receiver);

        Transaction receiverTxn = new CashInTransaction(receiver, amount, receiver.getWalletBalance(), sender.getPhoneNumber());
        transactionRepository.save(receiverTxn);

        Transaction senderTxn = new CashOutTransaction(sender, amount, sender.getWalletBalance(), destinationPhone);
        return transactionRepository.save(senderTxn);
    }

    @Override
    @Transactional
    public Transaction depositToSavings(Long userId, double amount) {
        User user = getUser(userId);

        if (user.getWalletBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient wallet balance to move to savings.");
        }
        user.moveToSavings(amount);
        userRepository.save(user);

        Transaction txn = new SavingsDepositTransaction(user, amount, user.getWalletBalance());
        return transactionRepository.save(txn);
    }

    @Override
    @Transactional
    public Transaction withdrawFromSavings(Long userId, double amount) {
        User user = getUser(userId);

        if (user.getSavingsBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient savings balance.");
        }
        user.withdrawFromSavings(amount);
        userRepository.save(user);

        Transaction txn = new SavingsWithdrawTransaction(user, amount, user.getWalletBalance());
        return transactionRepository.save(txn);
    }

    @Override
    public List<Transaction> getHistory(Long userId) {
        User user = getUser(userId);
        return transactionRepository.findByUserOrderByTimestampDesc(user);
    }

    @Override
    public List<Transaction> getRecentHistory(Long userId) {
        User user = getUser(userId);
        return transactionRepository.findTop5ByUserOrderByTimestampDesc(user);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
    }
}
