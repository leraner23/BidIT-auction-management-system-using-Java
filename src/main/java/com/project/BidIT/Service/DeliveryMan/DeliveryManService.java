package com.project.BidIT.Service.DeliveryMan;

import com.project.BidIT.entity.DeliveryMan;

import java.util.List;
import java.util.Optional;

public interface DeliveryManService {

    // Save a new delivery man
    DeliveryMan registerDeliveryMan(DeliveryMan deliveryMan);

    // Get all delivery men
    List<DeliveryMan> getAllDeliveryMen();
    DeliveryMan login(String email, String password);

    // Get delivery man by ID
    Optional<DeliveryMan> getDeliveryManById(long id);

    // Update delivery man details
    DeliveryMan updateDeliveryMan(DeliveryMan deliveryMan);

    // Delete delivery man
    void deleteDeliveryMan(long id);

    // Optional: find by email
    DeliveryMan findByDEmail(String email);

    // Optional: custom search, e.g., by status or rate
    List<DeliveryMan> findByStatus(String status);
}
