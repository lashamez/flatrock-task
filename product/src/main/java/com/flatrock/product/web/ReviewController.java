package com.flatrock.product.web;

import com.flatrock.product.domain.Review;
import com.flatrock.product.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/{productId}")
    public List<Review> getReviewsByProductId(@PathVariable Long productId) {
        return reviewService.getReviewsForProduct(productId);
    }

    @PostMapping("/{productId}")
    public Review addReview(@PathVariable Long productId, @RequestBody Review review) {
        return reviewService.saveReview(productId, review);
    }

    @PutMapping("/{reviewId}")
    public Review updateReview(@PathVariable Long reviewId, @RequestBody Review review) {
        return reviewService.updateReview(reviewId, review);
    }

    @DeleteMapping("/{reviewId}")
    public void deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
    }
}

