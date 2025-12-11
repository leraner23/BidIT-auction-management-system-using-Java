package com.project.BidIT.Service.Item;

import com.project.BidIT.entity.BidDetails;
import com.project.BidIT.entity.Item;

import java.util.List;

public interface ItemService {
    List<Item> getAllItems();
    Item getItemById(Long itemId);
    Item saveItem(Item item);
    void deleteItem(Long itemId);
    Item updateItem(Item item);
    Item markAsSold(Item item, BidDetails bidDetails);
}
