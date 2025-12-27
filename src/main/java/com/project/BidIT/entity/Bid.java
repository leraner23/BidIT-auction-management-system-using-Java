package com.project.BidIT.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bidId;

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "itemId")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "Bidder_id", referencedColumnName = "userId")
    private User user; // bidder



    private LocalDateTime bidTime;

    private double bidAmount;

    public double getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(double bidAmount) {
        this.bidAmount = bidAmount;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public LocalDateTime getBidTime() {
        return bidTime;
    }

    public void setBidTime(LocalDateTime bidTime) {
        this.bidTime = bidTime;
    }

    public Long getBidId() {
        return bidId;
    }

    public void setBidId(Long bidId) {
        this.bidId = bidId;
    }



    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
