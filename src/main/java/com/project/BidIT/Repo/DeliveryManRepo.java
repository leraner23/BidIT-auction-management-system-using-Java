package com.project.BidIT.Repo;

import com.project.BidIT.entity.DeliveryMan;
import com.project.BidIT.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryManRepo extends JpaRepository<DeliveryMan,Long> {

    DeliveryMan findByDEmailAndPassword(String dEmail, String password);
    DeliveryMan findByDEmail(String dEmail);
    List<DeliveryMan> findByStatus(Status status);
    List<DeliveryMan> findByCategory_CategoryId(Long categoryId);
    List<DeliveryMan> findByDNameContainingIgnoreCase(String dName);
}
