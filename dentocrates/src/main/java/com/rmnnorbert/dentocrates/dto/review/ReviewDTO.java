package com.rmnnorbert.dentocrates.dto.review;

import com.rmnnorbert.dentocrates.dao.review.Review;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReviewDTO(@Min(1) long id,
                        @NotNull String reviewer,
                        @NotNull String reviewedClinic,
                        @NotNull LocalDateTime reviewedAppointment,
                        int rating,
                        String review
) {
    public static ReviewDTO of(Review review) {
        int indexOfFirstCharacter = 0;
        return ReviewDTO.builder()
                .id(review.getId())
                .reviewer(review.getReviewer().getFirstName().charAt(indexOfFirstCharacter) +
                        ". " + review.getReviewer().getLastName())
                .reviewedClinic(review.getReviewedClinic().getName() +
                        ":" + review.getReviewedClinic().getDentistInContract().getFirstName() +
                        " " + review.getReviewedClinic().getDentistInContract().getLastName())
                .reviewedAppointment(review.getReviewedAppointment().getReservation())
                .rating(review.getRating())
                .review(review.getReview()).build();
    }
}
