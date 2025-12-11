package com.project.BidIT.Service.Item;

import com.project.BidIT.DTO.ItemDto;
import com.project.BidIT.Repo.BidDetailsRepo;
import com.project.BidIT.Repo.ItemRepository;
import com.project.BidIT.entity.BidDetails;
import com.project.BidIT.entity.Item;


import com.project.BidIT.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BidDetailsRepo bidDetailsRepository;

    @Override
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @Override
    public Item getItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found with ID: " + itemId));
    }

    @Override
    public Item saveItem(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public Item updateItem(Item item) {
        Item existing = getItemById(item.getItemId());

        if (existing.getBidDetails() == null) {
            existing.setAmount(item.getAmount());
            existing.setDescription(item.getDescription());
            existing.setRate(item.getRate());
            existing.setCategory(item.getCategory());
            existing.setItemImage(item.getItemImage());
            return itemRepository.save(existing);
        } else {
            throw new RuntimeException("Cannot update a sold item!");
        }
    }

    @Override
    public void deleteItem(Long itemId) {
        Item item = getItemById(itemId);


        if (item.getBidDetails() == null) {
            itemRepository.delete(item);
        } else {
            throw new RuntimeException("Cannot delete a sold item!");
        }
    }

    @Override
    public Item markAsSold(Item item, BidDetails bidDetails) {
        if (item.getBidDetails() != null) {
            throw new RuntimeException("Item is already sold!");
        }
        BidDetails savedBidDetails = bidDetailsRepository.save(bidDetails);
        item.setBidDetails(savedBidDetails);
        item.setStatus(Status.SOlD);
        return itemRepository.save(item);
    }
}

