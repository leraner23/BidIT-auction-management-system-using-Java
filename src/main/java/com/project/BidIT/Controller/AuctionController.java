package com.project.BidIT.Controller;

import com.project.BidIT.DTO.ItemDto;
import com.project.BidIT.Repo.BudgetRepo;
import com.project.BidIT.Repo.CategoryRepo;
import com.project.BidIT.Repo.ItemRepository;
import com.project.BidIT.Repo.UserRepository;
import com.project.BidIT.Service.BidService;
import com.project.BidIT.Service.CategoryService;
import com.project.BidIT.Service.Item.ItemService;
import com.project.BidIT.Service.User.UserService;
import com.project.BidIT.entity.*;

import com.project.BidIT.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/auction")
public class AuctionController {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ItemService itemService;

    @Autowired
    private BudgetRepo budgetRepository;
    @Autowired
    private BidService bidService;
    @GetMapping("/arena")
    public String getAuctionMap(Model model, Principal principal){
    if(principal == null) return "redirect:/login";
        String email = principal.getName();
        User users = userRepository.findByEmail(email).orElse(null);

        if (users == null) return "redirect:/login";
        model.addAttribute("users", users);
        model.addAttribute("items", itemRepository.findByStatus(Status.ACTIVE));
        return "BidWar/AuctionDashboard";
    }


    @GetMapping("/bidwar/{id}")
    public String BidwarDashboard(Model model, Principal principal, @PathVariable Long id) {

        if (principal == null) return "redirect:/login";

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Item item = itemService.getItemById(id);

        long remainingSeconds = itemService.getRemainingSeconds(item);
        // 🔹 STEP 2.2: FINALIZE AUCTION IF TIME IS OVER
        if (remainingSeconds == 0 && item.getStatus() != Status.SOlD) {
            bidService.finalizeAuction(item);
        }

        double highestBidAmount = bidService.getHighestBidForItem(item)
                .map(Bid::getBidAmount)
                .orElse(item.getAmount());

        Budget budget = budgetRepository.findByUser(user).orElse(null);

        double minBid = highestBidAmount + 100;
        double maxBid = (budget != null) ? budget.getTotal() : 0;

        boolean canBid = maxBid >= minBid;

        List<Bid> bids = bidService.getBidsForItem(item);
        bids.sort((b1, b2) -> Double.compare(b2.getBidAmount(), b1.getBidAmount()));

        // ✅ FETCH TRANSACTIONS
        List<Transaction> transactions =
                (budget != null) ? budget.getTransactions() : List.of();

        model.addAttribute("users", user);
        model.addAttribute("item", item);
        model.addAttribute("bids", bids);
        model.addAttribute("budget", budget);
        model.addAttribute("transactions", transactions);
        model.addAttribute("minBid", minBid);
        model.addAttribute("maxBid", maxBid);
        model.addAttribute("canBid", canBid);
        model.addAttribute("highestBidAmount", highestBidAmount);
        model.addAttribute("auctionStartTime", item.getAuctionStartTime());
        model.addAttribute("auctionDuration", item.getAuctionDurationMinutes());
        model.addAttribute("remainingSeconds", remainingSeconds);
        model.addAttribute("auctionEnded", remainingSeconds == 0);
        return "BidWar/WarDashboard";
    }

    @PostMapping("/bidwar/place")
    public String placeBid(@RequestParam("itemId") Long itemId,
                           @RequestParam("bidAmount") double bidAmount,
                           Principal principal,
                           Model model) {

        if (principal == null) return "redirect:/login";

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Item item = itemService.getItemById(itemId);
        Budget budget = budgetRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        LocalDateTime endTime =
                item.getAuctionStartTime()
                        .plusMinutes(item.getAuctionDurationMinutes());

        if (LocalDateTime.now().isAfter(endTime)) {
            model.addAttribute("bidError", "⏰ Auction has already ended!");
            return "redirect:/auction/bidwar/" + itemId;
        }
        try {
            bidService.placeBid(user, item, bidAmount);
            return "redirect:/auction/bidwar/" + itemId;

        } catch (IllegalArgumentException ex) {

            double highestBidAmount = bidService.getHighestBidForItem(item)
                    .map(Bid::getBidAmount)
                    .orElse(item.getAmount());

            model.addAttribute("bidError", ex.getMessage());
            model.addAttribute("openBidModal", true);
            model.addAttribute("users", user);
            model.addAttribute("item", item);
            model.addAttribute("budget", budget);
            model.addAttribute("transactions", budget.getTransactions());
            model.addAttribute("bids", bidService.getBidsForItem(item));
            model.addAttribute("highestBidAmount", highestBidAmount);

            return "BidWar/WarDashboard";
        }
    }


}
