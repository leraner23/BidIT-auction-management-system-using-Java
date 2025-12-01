package com.project.BidIT.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bidId;

    @ManyToOne
    @JoinColumn(name = "arena_id", referencedColumnName = "arenaId")
    private Arena arena;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user; // bidder

    @ManyToOne
    @JoinColumn(name = "Category" , referencedColumnName = "categoryId")
    private Category category;

    @Column(name = "Base-Amount", nullable = false)
    private double amount;

    @Column(name = "Image",nullable = false)
    private String itemImage;

    private LocalDateTime bidTime;

    public Long getBidId() {
        return bidId;
    }

    public void setBidId(Long bidId) {
        this.bidId = bidId;
    }

    public Arena getArena() {
        return arena;
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getBidTime() {
        return bidTime;
    }

    public void setBidTime(LocalDateTime bidTime) {
        this.bidTime = bidTime;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }
}
