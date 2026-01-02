package com.project.BidIT.Repo;

import com.project.BidIT.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepo  extends JpaRepository<Feedback,Long> {

}
