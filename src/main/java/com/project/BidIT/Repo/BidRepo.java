package com.project.BidIT.Repo;

import com.project.BidIT.entity.Bid;
import com.project.BidIT.entity.Item;
import com.project.BidIT.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BidRepo extends JpaRepository<Bid, Long> {


    // Get all bids by a user
    List<Bid> findByUser(User user);

    // Get all bids for a specific item
    List<Bid> findByItem(Item item);

    // Get the latest bid by a user
    Optional<Bid> findTopByUserOrderByBidTimeDesc(User user);

    // Get the highest bid for a specific item
    Optional<Bid> findTopByItemOrderByBidAmountDesc(Item item);
}
