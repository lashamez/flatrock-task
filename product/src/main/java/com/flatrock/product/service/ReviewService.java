package com.flatrock.product.service;

import com.flatrock.common.errors.BadRequestAlertException;
import com.flatrock.product.domain.Review;
import com.flatrock.product.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    private final StockService stockService;

    public ReviewService(ReviewRepository reviewRepository, StockService stockService) {
        this.reviewRepository = reviewRepository;
        this.stockService = stockService;
    }

    public List<Review> getReviewsForProduct(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    public Review saveReview(Long productId, Review review) {
        return stockService.findOneByProductId(productId).map(product -> reviewRepository.save(review))
                .orElseThrow(() -> new BadRequestAlertException("Product not found", "product", "productnotfound"));
    }

    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    public Review updateReview(Long reviewId, Review review) {
        return reviewRepository.findById(reviewId).map(rev -> reviewRepository.save(review))
                .orElseThrow();
    }

    // other methods as needed
}
