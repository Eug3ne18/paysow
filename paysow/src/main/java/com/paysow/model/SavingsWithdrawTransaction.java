package com.paysow.model;

import jakarta.persistence.Entity;

/** Concrete Transaction type: Savings money moved back into the wallet. */
@Entity
public class SavingsWithdrawTransaction extends Transaction {

    protected SavingsWithdrawTransaction() {
        super();
    }

    public SavingsWithdrawTransaction(User user, double amount, double balanceAfter) {
        super(user, amount, balanceAfter);
    }

    @Override
    public String getDescription() {
        return "Withdrawn from Savings";
    }

    @Override
    public String getTypeLabel() {
        return "SAVINGS OUT";
    }
}
