package com.project.BidIT.Repo;

import com.project.BidIT.entity.Item;
import com.project.BidIT.entity.Category;
import com.project.BidIT.entity.User;
import com.project.BidIT.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long> {

        @Query("SELECT COUNT(DISTINCT b.item) FROM BidDetails b WHERE b.user = :user")
        int countParticipatedItems(@Param("user") User user);

        @Query("SELECT COUNT(b) FROM BidDetails b WHERE b.user = :user")
        int totalBidsPlaced(@Param("user") User user);

        @Query("SELECT COUNT(b) FROM BidDetails b WHERE b.user = :user AND b.item.bidDetails.user = :user")
        int totalAuctionsWon(@Param("user") User user);

        // find all arenas created by a specific user (seller)
        List<Item> findByUser(User user);

        // find arenas by category
        List<Item> findByCategory(Category category);

        List<Item> findByStatus(Status status);
}
