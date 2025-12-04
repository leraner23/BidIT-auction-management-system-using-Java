package com.project.BidIT.DTOMapping;

import com.project.BidIT.DTO.DeliveryManDto;
import com.project.BidIT.entity.DeliveryMan;
import org.springframework.stereotype.Component;

@Component
public class DeliveryManMapping {

    // Map DTO to entity (for saving to database)
    public DeliveryMan mapDtoToEntity(DeliveryManDto dto) {
        DeliveryMan deliveryMan = new DeliveryMan();

        deliveryMan.setdName(dto.getdName());
        deliveryMan.setdEmail(dto.getdEmail());
        deliveryMan.setdPhone(dto.getdPhone());
        deliveryMan.setPassword(dto.getPassword());
        deliveryMan.setGender(dto.getGender());
        deliveryMan.setPAddress(dto.getPAddress());
        deliveryMan.setdAge(dto.getdAge());
        deliveryMan.setRate(dto.getRate());
        deliveryMan.setStatus(dto.getStatus());
        deliveryMan.setCategory(dto.getCategory());
        deliveryMan.setUser(dto.getUser());

        if (dto.getDeliveryImage() != null && !dto.getDeliveryImage().isEmpty()) {
            deliveryMan.setDeliveryImage(dto.getDeliveryImage().getOriginalFilename());
        }

        // rate, status, category, user will be set separately by admin/service
        return deliveryMan;
    }

    // Map entity to DTO (for displaying in frontend)
    public DeliveryManDto mapEntityToDto(DeliveryMan entity) {
        DeliveryManDto dto = new DeliveryManDto();

        dto.setdName(entity.getdName());
        dto.setdEmail(entity.getdEmail());
        dto.setdPhone(entity.getdPhone());
        dto.setPassword(entity.getPassword());
        dto.setGender(entity.getGender());
        dto.setPAddress(entity.getPAddress());
        dto.setdAge(entity.getdAge());
        dto.setCategory(entity.getCategory());
        dto.setRate(entity.getRate());
        dto.setStatus(entity.getStatus());
        dto.setUser(entity.getUser());

        // file upload cannot be mapped back to MultipartFile, you may handle this separately if needed
        return dto;
    }
}
