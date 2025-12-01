package com.project.BidIT.entity;

import com.project.BidIT.enums.Rate;
import com.project.BidIT.enums.Status;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class BidDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bidDetailsId;

    @ManyToOne
    @JoinColumn(name = "Bid", referencedColumnName = "bidId")
    private Bid bid;


    @ManyToOne
    @JoinColumn(name="buyer_id", referencedColumnName = "userId")
    private User buyer;

    @Column(name = "Amount Paied")
    private double amount;

    private String review;
    private String results;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "rate")
    private Rate rate;

    private  String description;

    @ManyToOne
    @JoinColumn(name = "Delivery_id", referencedColumnName = "deliveryId")
    private DeliveryMan deliveryMan;

    public DeliveryMan getDeliveryMan() {
        return deliveryMan;
    }

    public void setDeliveryMan(DeliveryMan deliveryMan) {
        this.deliveryMan = deliveryMan;
    }

    public Long getBidDetailsId() {
        return bidDetailsId;
    }

    public void setBidDetailsId(Long bidDetailsId) {
        this.bidDetailsId = bidDetailsId;
    }

    public Bid getBid() {
        return bid;
    }

    public void setBid(Bid bid) {
        this.bid = bid;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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
}
