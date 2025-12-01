package com.project.BidIT.entity;

import jakarta.persistence.*;

@Entity
public class Arena {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long arenaId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "Category", referencedColumnName = "categoryId")
    private Category category;

    @ManyToOne
    @JoinColumn(name ="Bids" , referencedColumnName = "bidId")
    private Bid bid;

    public long getArenaId() {
        return arenaId;
    }

    public void setArenaId(long arenaId) {
        this.arenaId = arenaId;
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

    public Bid getBid() {
        return bid;
    }

    public void setBid(Bid bid) {
        this.bid = bid;
    }
}
