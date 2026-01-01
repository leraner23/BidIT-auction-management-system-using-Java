package com.project.BidIT.entity;

import com.project.BidIT.enums.Rate;
import com.project.BidIT.enums.Status;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long itemId;

    @ManyToOne
    @JoinColumn(name = "Place_id", referencedColumnName = "userId")
    private User user;

    private String itemName;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @ManyToOne
    @JoinColumn(name = "Category", referencedColumnName = "categoryId")
    private Category category;

    @Column(nullable = true)
    private LocalDateTime auctionStartTime; // when admin starts auction

    @Column(nullable = true)
    private int auctionDurationMinutes;

    @OneToOne
    @JoinColumn(name = "bidDetails_id")
    private BidDetails bidDetails;

    @Column(name = "Image",nullable = false)
    private String itemImage;

    @Enumerated(EnumType.STRING)
    @Column(name = "rate")
    private Rate rate;

    private  String description;

    @Column(name = "Base-Amount", nullable = false)
    private double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public Rate getRate() {
        return rate;
    }

    public void setRate(Rate rate) {
        this.rate = rate;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }
    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


    public BidDetails getBidDetails() {
        return bidDetails;
    }

    public void setBidDetails(BidDetails bidDetails) {
        this.bidDetails = bidDetails;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
       this.itemName = itemName;
    }

    public LocalDateTime getAuctionStartTime() {
        return auctionStartTime;
    }

    public void setAuctionStartTime(LocalDateTime auctionStartTime) {
        this.auctionStartTime = auctionStartTime;
    }

    public int getAuctionDurationMinutes() {
        return auctionDurationMinutes;
    }

    public void setAuctionDurationMinutes(int auctionDurationMinutes) {
        this.auctionDurationMinutes = auctionDurationMinutes;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
}
