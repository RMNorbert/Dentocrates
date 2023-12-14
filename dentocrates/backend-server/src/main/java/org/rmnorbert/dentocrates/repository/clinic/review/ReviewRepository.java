package org.rmnorbert.dentocrates.repository.clinic.review;


import org.rmnorbert.dentocrates.dao.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    @Query("SELECT r FROM Review r WHERE r.reviewedClinic.id = :id")
    List<Review> getAllReviewByClinic(long id);
    List<Review> getAllByReviewerId(long id);
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.reviewedClinic.id = :id")
    Double getRatingByClinic(long id);
}
