package com.project.BidIT.Repo;

import com.project.BidIT.entity.Item;
import com.project.BidIT.entity.Category;
import com.project.BidIT.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long> {



        // find all arenas created by a specific user (seller)
        List<Item> findByUser(User user);

        // find arenas by category
        List<Item> findByCategory(Category category);


}
