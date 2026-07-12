package com.paysow.repository;

import com.paysow.model.Transaction;
import com.paysow.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserOrderByTimestampDesc(User user);
    List<Transaction> findTop5ByUserOrderByTimestampDesc(User user);
}
