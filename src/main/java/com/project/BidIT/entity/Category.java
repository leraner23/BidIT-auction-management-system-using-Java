package com.project.BidIT.entity;

import jakarta.persistence.*;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long categoryId;

    @Column(name ="Category_name", nullable = false, unique = true)
    private String name;

    @Column(name = "Qunatity")
    private double quantity;

    @Column(name = "base_price", nullable = false)
    private float bPrice;

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public float getbPrice() {
        return bPrice;
    }

    public void setbPrice(float bPrice) {
        this.bPrice = bPrice;
    }
}
