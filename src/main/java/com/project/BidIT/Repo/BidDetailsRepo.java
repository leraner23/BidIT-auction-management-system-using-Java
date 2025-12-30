package com.project.BidIT.Repo;

import com.project.BidIT.entity.Bid;
import com.project.BidIT.entity.BidDetails;
import com.project.BidIT.entity.DeliveryMan;
import com.project.BidIT.entity.User;
import com.project.BidIT.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidDetailsRepo extends JpaRepository<BidDetails, Long> {

    List<BidDetails> findByBid(Bid bid);
    List<BidDetails> findByBuyer(User buyer);
    List<BidDetails> findByDeliveryMan(DeliveryMan deliveryMan);


    boolean existsByBid(Bid bid);
}