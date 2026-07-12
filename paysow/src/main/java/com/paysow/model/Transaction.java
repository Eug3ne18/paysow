package com.paysow.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * OOP PILLAR: ABSTRACTION
 * ---------------------------------------------------------
 * Transaction defines WHAT every wallet activity must provide
 * (a description and a type label) without saying HOW each kind
 * of activity produces that description. Callers (like the history
 * page) only ever need to know "this is a Transaction" - they don't
 * need to know or care whether it's a cash-in, cash-out, or savings
 * move under the hood.
 *
 * OOP PILLAR: INHERITANCE
 * ---------------------------------------------------------
 * CashInTransaction, CashOutTransaction, SavingsDepositTransaction and
 * SavingsWithdrawTransaction all extend this class and reuse its
 * id/user/amount/balanceAfter/timestamp fields instead of redefining them.
 */
@Entity
@Table(name = "transactions")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private double balanceAfter;

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    protected Transaction() {
        // required by JPA
    }

    protected Transaction(User user, double amount, double balanceAfter) {
        this.user = user;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
    }

    /**
     * OOP PILLAR: POLYMORPHISM
     * Every subclass overrides this method with its own logic.
     * The history page calls txn.getDescription() on a plain
     * "Transaction" reference and Java automatically runs the
     * correct subclass version at runtime.
     */
    public abstract String getDescription();

    public abstract String getTypeLabel();

    public Long getId() { return id; }
    public User getUser() { return user; }
    public double getAmount() { return amount; }
    public double getBalanceAfter() { return balanceAfter; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
