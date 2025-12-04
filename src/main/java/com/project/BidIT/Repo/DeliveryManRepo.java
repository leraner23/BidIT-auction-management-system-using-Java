package com.project.BidIT.Repo;

import com.project.BidIT.entity.DeliveryMan;
import com.project.BidIT.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryManRepo extends JpaRepository<DeliveryMan,Long> {
    Optional<DeliveryMan> findByDEmail(String email);

    List<DeliveryMan> findByStatus(String status);


}
