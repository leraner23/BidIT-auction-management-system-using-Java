package com.project.BidIT.Repo;
import com.project.BidIT.entity.Budget;
import com.project.BidIT.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepo extends JpaRepository<Budget, Long> {

    @Query("SELECT COALESCE(SUM(b.total), 0) FROM Budget b")
    double getTotalBudget();


    List<Budget> findByUser_UserId(Long userId);
    Optional<Budget> findByUser(User user);
}
