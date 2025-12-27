package com.project.BidIT.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long transactionId;

    @ManyToOne
    @JoinColumn(name = "budget_id", nullable = false)
    private Budget budget;

    @Column(nullable = false)
    private float amount;

    @Column(nullable = false)
    private String transactionType; // CREDIT / DEBIT

    @Column(name = "payment_from", nullable = false)
    private String accountInfo;

    @Column(nullable = false)
    private LocalDateTime time;


    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    @Column(name = "payment_gateway_id", nullable = false, unique = true)
    private String paymentGatewayId;

    public String getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(String accountInfo) {
        this.accountInfo = accountInfo;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getPaymentGatewayId() {
        return paymentGatewayId;
    }

    public void setPaymentGatewayId(String paymentGatewayId) {
        this.paymentGatewayId = paymentGatewayId;
    }
}
