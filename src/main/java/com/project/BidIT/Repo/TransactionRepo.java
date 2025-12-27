package com.project.BidIT.Repo;

import com.project.BidIT.entity.Budget;
import com.project.BidIT.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Long> {

    List<Transaction> findByBudgetOrderByTimeDesc(Budget budget);
    Transaction findByPaymentGatewayId(String paymentGatewayId);
}
