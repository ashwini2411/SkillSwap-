package com.skillswap.platform.repository;

import com.skillswap.platform.entity.Review;
import com.skillswap.platform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByRevieweeOrderByCreatedAtDesc(User reviewee);
}
