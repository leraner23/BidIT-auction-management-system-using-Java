package com.project.BidIT.Service;

import com.project.BidIT.Repo.BidDetailsRepo;
import com.project.BidIT.Repo.BidRepo;

import com.project.BidIT.Repo.BudgetRepo;
import com.project.BidIT.Repo.ItemRepository;
import com.project.BidIT.entity.*;
import com.project.BidIT.enums.Status;
import jakarta.transaction.Transactional;
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
    private ItemRepository itemRepository;
    @Autowired
    private BidDetailsRepo bidDetailsRepo;
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


    public void deleteBidDetails(Long bidDetailsId) {

        BidDetails bidD = bidDetailsRepo.findById(bidDetailsId)
                .orElseThrow(() -> new RuntimeException("Bid details not found"));

        Item item = bidD.getBid().getItem();

        if (item.getStatus() == Status.SOlD) {
            throw new RuntimeException("Cannot delete a sold item!");
        }

        bidDetailsRepo.delete(bidD);
    }


    @Transactional
    public void finalizeAuction(Item item) {

        // 1️⃣ Prevent duplicate finalization
        if (item.getStatus() == Status.SOlD) {
            return;
        }


        // 2️⃣ Find highest bid for the item
        Optional<Bid> highestBidOpt =
                bidRepo.findTopByItemOrderByBidAmountDesc(item);

        // 3️⃣ If no bids placed → mark auction expired
        if (highestBidOpt.isEmpty()) {
            item.setStatus(Status.EXPIRED); // use SOLD if you don't have EXPIRED
            itemRepository.save(item);
            return;
        }

        Bid highestBid = highestBidOpt.get();
        User winner = highestBid.getUser();
        double finalAmount = highestBid.getBidAmount();
        Budget winnerBudget = budgetRepository.findByUser(winner)
                .orElseThrow(() -> new RuntimeException("Winner has no budget!"));

        if (winnerBudget.getTotal() < finalAmount) {
            throw new RuntimeException("Winner does not have enough budget!");
        }

        float finalBidAmount = (float) finalAmount;
        winnerBudget.setTotal(winnerBudget.getTotal() - finalBidAmount);
        budgetRepository.save(winnerBudget);
        // 4️⃣ Create BidDetails (winner info)
        BidDetails bidDetails = new BidDetails();
        bidDetails.setBid(highestBid);

        bidDetails.setBuyer(highestBid.getUser());
        bidDetails.setAmount(highestBid.getBidAmount());

        bidDetailsRepo.save(bidDetails);

        // 5️⃣ Update item
        item.setStatus(Status.SOlD);
        item.setBidDetails(bidDetails);

        itemRepository.save(item);
    }
}
