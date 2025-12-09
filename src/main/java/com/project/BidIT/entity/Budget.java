package com.project.BidIT.entity;

import jakarta.persistence.*;


import java.time.LocalDate;
import java.util.List;

@Entity
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long budgetId;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;

    @Column(name="Amount",nullable = false)
    private float amount;

    @Column(name = "payment_from", nullable = false)
    private String accountInfo;

    @Column(name = "Time", nullable = false)
    private LocalDate time;

    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL)
    private List<BudgetTranscation> transactions;

    public List<BudgetTranscation> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<BudgetTranscation> transactions) {
        this.transactions = transactions;
    }

    public long getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(long budgetId) {
        this.budgetId = budgetId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(String accountInfo) {
        this.accountInfo = accountInfo;
    }

    public LocalDate getTime() {
        return time;
    }

    public void setTime(LocalDate time) {
        this.time = time;
    }
}
