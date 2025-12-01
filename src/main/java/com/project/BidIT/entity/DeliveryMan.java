package com.project.BidIT.entity;

import com.project.BidIT.enums.Rate;
import com.project.BidIT.enums.Status;
import jakarta.persistence.*;

@Entity
public class DeliveryMan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long deliveryId;

    @Column(name = "DeliverBoy", nullable = false)
    private String dName;

    @Column(name = "Email", nullable = false, unique = true)
    private String dEmail;
    @Column(name = "Phone", nullable = false)
    private String dPhone;
    @Column(nullable = false)
    private String deliveryImage;

    @Column(name="Password", nullable = false)
    private String password;

    private String gender;
    @Column(name = "Permanent_Residence", nullable = false)
    private String PAddress;

    @Column(name = "Age", nullable = false)
    private String dAge;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "Category", referencedColumnName = "categoryId")
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(name="status",nullable = false)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "Rate", nullable = false)
    private Rate rate;

    public long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getdName() {
        return dName;
    }

    public void setdName(String dName) {
        this.dName = dName;
    }

    public String getdEmail() {
        return dEmail;
    }

    public void setdEmail(String dEmail) {
        this.dEmail = dEmail;
    }

    public String getdPhone() {
        return dPhone;
    }

    public void setdPhone(String dPhone) {
        this.dPhone = dPhone;
    }

    public String getDeliveryImage() {
        return deliveryImage;
    }

    public void setDeliveryImage(String deliveryImage) {
        this.deliveryImage = deliveryImage;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPAddress() {
        return PAddress;
    }

    public void setPAddress(String PAddress) {
        this.PAddress = PAddress;
    }

    public String getdAge() {
        return dAge;
    }

    public void setdAge(String dAge) {
        this.dAge = dAge;
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
}
