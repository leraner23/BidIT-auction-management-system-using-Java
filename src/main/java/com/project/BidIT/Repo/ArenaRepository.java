package com.project.BidIT.Repo;

import com.project.BidIT.entity.Arena;
import com.project.BidIT.entity.Category;
import com.project.BidIT.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArenaRepository extends JpaRepository<Arena,Long> {



        // find all arenas created by a specific user (seller)
        List<Arena> findByUser(User user);

        // find arenas by category
        List<Arena> findByCategory(Category category);


}
