package com.project.BidIT.Service.DeliveryMan;

import com.project.BidIT.Repo.DeliveryManRepo;
import com.project.BidIT.entity.DeliveryMan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeliveryManServiceImpl implements DeliveryManService {

    @Autowired
    private DeliveryManRepo deliveryManRepository;

    @Override
    public DeliveryMan registerDeliveryMan(DeliveryMan deliveryMan) {
        return deliveryManRepository.save(deliveryMan);
    }

    public DeliveryMan login(String email, String password) {
        return deliveryManRepository.findByDEmail(email)
                .filter(dm -> dm.getPassword().equals(password)).orElseThrow(null);

    }


    @Override
    public List<DeliveryMan> getAllDeliveryMen() {
        return deliveryManRepository.findAll();
    }

    @Override
    public Optional<DeliveryMan> getDeliveryManById(long id) {
        return deliveryManRepository.findById(id);
    }

    @Override
    public DeliveryMan updateDeliveryMan(DeliveryMan deliveryMan) {
        return deliveryManRepository.save(deliveryMan);
    }

    @Override
    public void deleteDeliveryMan(long id) {
        deliveryManRepository.deleteById(id);
    }

    @Override
    public DeliveryMan findByDEmail(String email) {
        return deliveryManRepository.findByDEmail(email).orElse(null);
    }

    @Override
    public List<DeliveryMan> findByStatus(String status) {
        return deliveryManRepository.findByStatus(status);
    }
}
