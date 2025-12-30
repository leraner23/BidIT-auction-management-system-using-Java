package com.project.BidIT.DTO;

import com.project.BidIT.entity.Bid;
import com.project.BidIT.entity.BidDetails;
import com.project.BidIT.entity.Category;
import com.project.BidIT.entity.User;
import com.project.BidIT.enums.Rate;
import com.project.BidIT.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public class ItemDto {
    private Long itemId;

    private User user;

    private String itemName;
    private Long categoryId;

    private Bid bid;

    private BidDetails bidDetails;
    private MultipartFile itemImage;

    private Rate rate;

    private String description;

    private int auctionDurationMinutes;
    private LocalDateTime auctionStartTime;

    @Positive
    private double amount;

    private Status status;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Bid getBid() {
        return bid;
    }

    public void setBid(Bid bid) {
        this.bid = bid;
    }

    public MultipartFile getItemImage() {
        return itemImage;
    }

    public void setItemImage(MultipartFile itemImage) {
        this.itemImage = itemImage;
    }

    public Rate getRate() {
        return rate;
    }

    public void setRate(Rate rate) {
        this.rate = rate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public BidDetails getBidDetails() {
        return bidDetails;
    }

    public void setBidDetails(BidDetails bidDetails) {
        this.bidDetails = bidDetails;
    }

    public int getAuctionDurationMinutes() {
        return auctionDurationMinutes;
    }

    public void setAuctionDurationMinutes(int auctionDurationMinutes) {
        this.auctionDurationMinutes = auctionDurationMinutes;
    }

    public LocalDateTime getAuctionStartTime() {
        return auctionStartTime;
    }

    public void setAuctionStartTime(LocalDateTime auctionStartTime) {
        this.auctionStartTime = auctionStartTime;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
