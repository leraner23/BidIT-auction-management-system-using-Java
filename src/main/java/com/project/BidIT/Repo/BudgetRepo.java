package com.project.BidIT.Repo;
import com.project.BidIT.entity.Budget;
import com.project.BidIT.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetRepo extends JpaRepository<Budget, Long> {

    List<Budget> findByUser(User user);
    List<Budget> findByUser_UserId(Long userId);

}
