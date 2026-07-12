package com.paysow.model;

import jakarta.persistence.Entity;

/** Concrete Transaction type: money going OUT of the wallet to another phone number. */
@Entity
public class CashOutTransaction extends Transaction {

    private String destinationPhone;

    protected CashOutTransaction() {
        super();
    }

    public CashOutTransaction(User user, double amount, double balanceAfter, String destinationPhone) {
        super(user, amount, balanceAfter);
        this.destinationPhone = destinationPhone;
    }

    @Override
    public String getDescription() {
        return "Sent to " + destinationPhone;
    }

    @Override
    public String getTypeLabel() {
        return "CASH OUT";
    }

    public String getDestinationPhone() { return destinationPhone; }
}
