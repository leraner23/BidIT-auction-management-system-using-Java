package com.project.BidIT.Service;

import com.project.BidIT.Repo.BidRepo;

import com.project.BidIT.Repo.BudgetRepo;
import com.project.BidIT.entity.Bid;
import com.project.BidIT.entity.Budget;
import com.project.BidIT.entity.Item;
import com.project.BidIT.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BidService {

    @Autowired
    private BidRepo bidRepo;

    @Autowired
    private BudgetRepo budgetRepository;
    // Place a new bid
    public Bid placeBid(User user, Item item, double amount) {

        // 1. Get user's budget
        Budget userBudget = budgetRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("User has no budget!"));

        // 2. Check if user has enough budget
        if (userBudget.getTotal() < amount) {
            throw new IllegalArgumentException("Not enough budget to place this bid!");
        }

        // Optional: check highest existing bid for the item
        Optional<Bid> highestBidOpt = bidRepo.findTopByItemOrderByBidAmountDesc(item);

        if (highestBidOpt.isPresent() && amount <= highestBidOpt.get().getBidAmount()) {
            throw new IllegalArgumentException("Bid amount must be higher than current highest bid!");
        }

        // Create and save the new bid
        Bid newBid = new Bid();
        newBid.setUser(user);
        newBid.setItem(item);
        newBid.setBidAmount(amount);
        newBid.setBidTime(LocalDateTime.now());

        return bidRepo.save(newBid);
    }

    public List<Bid> getBidsByUser(User user) {
        return bidRepo.findByUser(user);
    }

    public List<Bid> getBidsForItem(Item item) {
        return bidRepo.findByItem(item);
    }

    public Optional<Bid> getLatestBidByUser(User user) {
        return bidRepo.findTopByUserOrderByBidTimeDesc(user);
    }

    public Optional<Bid> getHighestBidForItem(Item item) {
        return bidRepo.findTopByItemOrderByBidAmountDesc(item);
    }

    public int getNextBidNumber(Item item) {
        return getBidsForItem(item).size() + 1; // 1-based count
    }

    public int getBidTimeLimit(Item item) {
        int nextBid = getNextBidNumber(item);
        if (nextBid == 1) return 30;
        if (nextBid == 2) return 40;
        return 0; // After 3rd bid, bidding closed
    }
}
