package com.rmnnorbert.dentocrates.repository;

import com.rmnnorbert.dentocrates.dao.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    List<Review> getAllByReviewedClinicId(long id);
    List<Review> getAllByReviewerId(long id);
}
