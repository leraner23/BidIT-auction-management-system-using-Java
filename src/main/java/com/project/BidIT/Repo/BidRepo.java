package com.project.BidIT.Repo;

import com.project.BidIT.entity.Bid;
import com.project.BidIT.entity.Item;
import com.project.BidIT.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BidRepo extends JpaRepository<Bid, Long> {
    // Total bids placed by user
    int countByUser(User user);

    // Total unique auctions participated in by user
    @Query("SELECT COUNT(DISTINCT b.item) FROM BidDetails b WHERE b.user = :user")
    int countParticipatedItems(@Param("user") User user);

    // Total auctions won by user
    @Query("SELECT COUNT(b) FROM BidDetails b WHERE b.user = :user AND b.item.bidDetails.user = :user")
    int totalAuctionsWon(@Param("user") User user);

    // Total auctions lost by user
    @Query("SELECT COUNT(b) FROM BidDetails b WHERE b.user = :user AND b.item.bidDetails.user != :user")
    int totalAuctionsLost(@Param("user") User user);

    // Get all bids by a user
    List<Bid> findByUser(User user);

    // Get all bids for a specific item
    List<Bid> findByItem(Item item);


    // Get the latest bid by a user
    Optional<Bid> findTopByUserOrderByBidTimeDesc(User user);

    // Get the highest bid for a specific item
    Optional<Bid> findTopByItemOrderByBidAmountDesc(Item item);
}
