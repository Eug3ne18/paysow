package com.paysow.model;

import jakarta.persistence.Entity;

/** Concrete Transaction type: wallet money moved into Savings/Investment. */
@Entity
public class SavingsDepositTransaction extends Transaction {

    protected SavingsDepositTransaction() {
        super();
    }

    public SavingsDepositTransaction(User user, double amount, double balanceAfter) {
        super(user, amount, balanceAfter);
    }

    @Override
    public String getDescription() {
        return "Transferred to Savings";
    }

    @Override
    public String getTypeLabel() {
        return "SAVINGS IN";
    }
}
