package com.project.BidIT.Controller;

import com.project.BidIT.Repo.BudgetRepo;
import com.project.BidIT.Repo.UserRepository;
import com.project.BidIT.entity.Budget;
import com.project.BidIT.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/budget")
public class BudgetController {

    @Autowired
    private BudgetRepo budgetRepo;

    @Autowired
    private UserRepository userRepository;

    // Load dashboard
    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {

        if (principal == null) return "redirect:/login";

        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) return "redirect:/login";

        // Fetch budgets
        List<Budget> budgets = budgetRepo.findByUser_UserId(user.getUserId());

        // Calculate total
        float total = 0;
        for (Budget b : budgets) {
            total += b.getAmount();
        }

        model.addAttribute("user", user);
        model.addAttribute("budgets", budgets);
        model.addAttribute("totalBudget", total);

        return "BudgetDashboard";
    }

    // When PayPal success — JS sends POST request
    @PostMapping("/pay-success")
    @ResponseBody
    public String saveSuccess(@RequestBody Map<String, String> payload, Principal principal) {

        if (principal == null) return "ERROR: User not logged in";

        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return "ERROR: User not found";

        float amount = Float.parseFloat(payload.get("amount"));
        String accountInfo = payload.get("accountInfo");
        String transactionId = payload.get("transactionId");

        Budget b = new Budget();
        b.setUser(user);
        b.setAmount(amount);
        b.setAccountInfo(accountInfo + " | TXN: " + transactionId);
        b.setTime(LocalDate.now());

        budgetRepo.save(b);

        return "SUCCESS";
    }
}
