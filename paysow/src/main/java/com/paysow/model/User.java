package com.paysow.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * OOP PILLAR: ENCAPSULATION
 * ---------------------------------------------------------
 * All fields are PRIVATE. Nothing outside this class can reach in and
 * set walletBalance or savingsBalance directly to an arbitrary number.
 * Instead, the class exposes controlled behaviour (credit, debit,
 * moveToSavings, withdrawFromSavings) that protects the data's integrity
 * (e.g. you can never debit more than the current balance).
 * The raw 4-digit PIN is never stored anywhere - only its hash is,
 * and it can only be checked via checkPin(), never read back out.
 */
@Entity
@Table(name = "app_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String pinHash;

    @Column(nullable = false)
    private double walletBalance = 0.0;

    @Column(nullable = false)
    private double savingsBalance = 0.0;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions = new ArrayList<>();

    protected User() {
        // required by JPA
    }

    public User(String fullName, String email, String phoneNumber, String pinHash) {
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.pinHash = pinHash;
    }

    // ---- Encapsulated, guarded balance operations ----

    public void credit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be greater than zero.");
        this.walletBalance += amount;
    }

    public void debit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be greater than zero.");
        if (this.walletBalance < amount) throw new IllegalStateException("Insufficient wallet balance.");
        this.walletBalance -= amount;
    }

    public void moveToSavings(double amount) {
        debit(amount);
        this.savingsBalance += amount;
    }

    public void withdrawFromSavings(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be greater than zero.");
        if (this.savingsBalance < amount) throw new IllegalStateException("Insufficient savings balance.");
        this.savingsBalance -= amount;
        this.walletBalance += amount;
    }

    // ---- Getters / Setters (the only doorway into the private state) ----

    public Long getId() { return id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getPinHash() { return pinHash; }
    public void setPinHash(String pinHash) { this.pinHash = pinHash; }

    public double getWalletBalance() { return walletBalance; }
    public double getSavingsBalance() { return savingsBalance; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public List<Transaction> getTransactions() { return transactions; }
}
