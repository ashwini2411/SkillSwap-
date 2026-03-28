package com.skillswap.platform.controller;

import com.skillswap.platform.entity.Review;
import com.skillswap.platform.entity.User;
import com.skillswap.platform.repository.ReviewRepository;
import com.skillswap.platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/reviews/{targetUserId}")
    public String addReview(@PathVariable Long targetUserId,
                            @RequestParam("rating") Integer rating,
                            @RequestParam("feedback") String feedback,
                            Principal principal) {
        if (principal == null) return "redirect:/login";

        User reviewer = userRepository.findByUsername(principal.getName()).orElse(null);
        User reviewee = userRepository.findById(targetUserId).orElse(null);

        if (reviewer != null && reviewee != null && !reviewer.getId().equals(reviewee.getId())) {
            
            // Basic validation
            if (rating < 1) rating = 1;
            if (rating > 5) rating = 5;

            // Create Review
            Review review = new Review();
            review.setReviewer(reviewer);
            review.setReviewee(reviewee);
            review.setRating(rating);
            review.setFeedback(feedback);
            reviewRepository.save(review);
            
            // Calculate Points (Rating * 10)
            int earnedPoints = rating * 10;
            Integer currentPoints = reviewee.getPoints();
            if (currentPoints == null) currentPoints = 0;
            reviewee.setPoints(currentPoints + earnedPoints);
            
            // Recalculate Average Rating
            List<Review> allReviews = reviewRepository.findByRevieweeOrderByCreatedAtDesc(reviewee);
            double avg = allReviews.stream().mapToInt(Review::getRating).average().orElse(0.0);
            
            // Round to 1 decimal place
            avg = Math.round(avg * 10.0) / 10.0;
            reviewee.setRating(avg);
            
            userRepository.save(reviewee);
        }

        return "redirect:/profile/" + targetUserId + "?success=ReviewAdded";
    }
}
