package com.project.BidIT.Controller;

import com.project.BidIT.Repo.BudgetRepo;
import com.project.BidIT.Repo.TransactionRepo;
import com.project.BidIT.Repo.UserRepository;
import com.project.BidIT.entity.Budget;

import com.project.BidIT.entity.Transaction;
import com.project.BidIT.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/budget")
public class BudgetController {

    @Autowired
    private BudgetRepo budgetRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepo transactionRepo;
    // Load dashboard
    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {

        if (principal == null) return "redirect:/login";

        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) return "redirect:/login";

        // Fetch budgets
        Budget budgets = budgetRepo.findByUser(user).orElse(null);

        // Calculate total

        List<Transaction> transactions =
                (budgets != null) ? budgets.getTransactions() : List.of();

        model.addAttribute("user", user);
        model.addAttribute("budget", budgets);
        model.addAttribute("transactions",transactions);


        return "BudgetDashboard";
    }

    @PostMapping("/success")
    public String Return(Model model,@RequestBody  Map<String, String> payload, Principal principal) {
        try {
            if (principal == null) return "ERROR: User not logged in";

            String email = principal.getName();
            User user = userRepository.findByEmail(email).orElse(null);
            if (user == null) return "ERROR: User not found";


            Budget b = budgetRepo.findByUser(user)
                    .orElseGet(() -> {
                        Budget newBudget = new Budget();
                        newBudget.setUser(user);
                        newBudget.setTotal(0);
                        return budgetRepo.save(newBudget);
                    });

            float amount = Float.parseFloat(payload.get("amount"));
            String accountInfo = payload.get("accountInfo");
            String transactionInfo = payload.get("transactionInfo");
            String transactionType = payload.get("transactionType");



            if ("CREDIT".equalsIgnoreCase(transactionType)) {
                b.setTotal(b.getTotal() + amount);
            } else if ("DEBIT".equalsIgnoreCase(transactionType)) {
                if (b.getTotal() < amount) {
                    return "ERROR: Insufficient balance";
                }
                b.setTotal(b.getTotal() - amount);
            } else {
                return "ERROR: Invalid transaction type";
            }
            Transaction t = new Transaction();
            t.setAmount(amount);
            t.setPaymentGatewayId(transactionInfo);
            t.setAccountInfo(accountInfo + " | TXN: " + transactionInfo + "type :" +transactionType);
            t.setTransactionType(transactionType);
            t.setTime(LocalDateTime.now());
            budgetRepo.save(b);
            t.setBudget(b);

            transactionRepo.save(t);
            return "SUCCESS";
        } catch (Exception e) {

            return "ERROR: " + e.getMessage();
        }

    }
}
