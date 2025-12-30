package com.project.BidIT.DTOMapping;

import com.project.BidIT.DTO.ItemDto;
import com.project.BidIT.entity.Category;
import com.project.BidIT.entity.Item;
import com.project.BidIT.Repo.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemMapping {

    @Autowired
    private CategoryRepo categoryRepo;

    public Item toEntity(ItemDto dto, String savedImageName) {
        Item item = new Item();

        item.setItemId(dto.getItemId());
        item.setUser(dto.getUser());

        // fetch Category entity from DB
        Category category = categoryRepo.findById(dto.getCategoryId()).orElse(null);
        item.setCategory(category);
        item.setBidDetails(dto.getBidDetails());
        // optional
        item.setRate(dto.getRate());
        item.setDescription(dto.getDescription());
        item.setAmount(dto.getAmount());
        item.setStatus(dto.getStatus());
        item.setItemImage(savedImageName);  // must be stored manually
        item.setAuctionDurationMinutes(dto.getAuctionDurationMinutes());
        item.setAuctionStartTime(dto.getAuctionStartTime());
        item.setItemName(dto.getItemName());
        return item;
    }

    public ItemDto toDto(Item item) {
        ItemDto dto = new ItemDto();

        dto.setItemId(item.getItemId());
        dto.setUser(item.getUser());
        dto.setBidDetails(item.getBidDetails());
        dto.setCategoryId(item.getCategory() != null ? item.getCategory().getCategoryId() : null);

        dto.setRate(item.getRate());
        dto.setDescription(item.getDescription());
        dto.setAmount(item.getAmount());
        dto.setStatus(item.getStatus());
        dto.setAuctionDurationMinutes(item.getAuctionDurationMinutes());
        dto.setAuctionStartTime(item.getAuctionStartTime());
        dto.setItemName(item.getItemName());

        return dto;
    }
}
