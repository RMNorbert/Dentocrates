package com.rmnnorbert.dentocrates.controller.clinic;

import com.rmnnorbert.dentocrates.dto.DeleteDTO;
import com.rmnnorbert.dentocrates.dto.review.ReviewDTO;
import com.rmnnorbert.dentocrates.dto.review.ReviewRegisterDTO;
import com.rmnnorbert.dentocrates.service.review.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;
    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/customer/{id}")
    public List<ReviewDTO> getCustomerReviews(@PathVariable Long id ){
        return reviewService.getAllReviewById(id);
    }
    @GetMapping("/clinic/{id}")
    public List<ReviewDTO> getReviewsByClinic(@PathVariable Long id ){
        return reviewService.getAllReviewByClinic(id);
    }

    @GetMapping("/rating/clinic/{id}")
    public Double getRatingByClinic(@PathVariable Long id ){
        return reviewService.getRatingByClinic(id);
    }
    @PostMapping("/register")
    public ResponseEntity<String> addReview(@Valid @RequestBody ReviewRegisterDTO dto){
        return reviewService.registerReview(dto);
    }
    @DeleteMapping("/")
    public ResponseEntity<String> removeReview(@Valid @RequestBody DeleteDTO dto){
        return reviewService.deleteReviewById(dto);
    }

}
