package com.paysow.model;

import jakarta.persistence.Entity;

/** Concrete Transaction type: money coming INTO the wallet. */
@Entity
public class CashInTransaction extends Transaction {

    private String source; // e.g. "Top-up" or the sender's phone number

    protected CashInTransaction() {
        super();
    }

    public CashInTransaction(User user, double amount, double balanceAfter, String source) {
        super(user, amount, balanceAfter);
        this.source = source;
    }

    @Override
    public String getDescription() {
        if (source == null || source.isBlank() || source.equalsIgnoreCase("Top-up")) {
            return "Cash In (Top-up)";
        }
        return "Received from " + source;
    }

    @Override
    public String getTypeLabel() {
        return "CASH IN";
    }

    public String getSource() { return source; }
}
