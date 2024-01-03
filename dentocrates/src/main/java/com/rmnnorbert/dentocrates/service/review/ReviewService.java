package com.rmnnorbert.dentocrates.service.review;

import com.rmnnorbert.dentocrates.dao.client.Customer;
import com.rmnnorbert.dentocrates.dao.appointmentCalendar.AppointmentCalendar;
import com.rmnnorbert.dentocrates.dao.clinic.Clinic;
import com.rmnnorbert.dentocrates.dao.review.Review;
import com.rmnnorbert.dentocrates.dto.DeleteDTO;
import com.rmnnorbert.dentocrates.dto.review.ReviewDTO;
import com.rmnnorbert.dentocrates.dto.review.ReviewRegisterDTO;
import com.rmnnorbert.dentocrates.repository.clinic.appointmentCalendar.AppointmentCalendarRepository;
import com.rmnnorbert.dentocrates.repository.clinic.ClinicRepository;
import com.rmnnorbert.dentocrates.repository.client.CustomerRepository;
import com.rmnnorbert.dentocrates.repository.clinic.review.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.text.DecimalFormat;
import java.util.List;

import static com.rmnnorbert.dentocrates.controller.ApiResponseConstants.*;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final AppointmentCalendarRepository appointmentCalendarRepository;
    private final CustomerRepository customerRepository;
    private final ClinicRepository clinicRepository;
    private final static Double WITHOUT_REVIEW_RATING_VALUE = 0.0;
    @Autowired
    public ReviewService(ReviewRepository reviewRepository, AppointmentCalendarRepository appointmentCalendarRepository, CustomerRepository customerRepository, ClinicRepository clinicRepository) {
        this.reviewRepository = reviewRepository;
        this.appointmentCalendarRepository = appointmentCalendarRepository;
        this.customerRepository = customerRepository;
        this.clinicRepository = clinicRepository;
    }

    public List<ReviewDTO> getAllReviewById(Long id) {
        return reviewRepository.getAllByReviewerId(id)
                .stream()
                .map(ReviewDTO::of)
                .toList();
    }
    public List<ReviewDTO> getAllReviewByClinic(Long id) {
        return reviewRepository.getAllReviewByClinic(id)
                .stream()
                .map(ReviewDTO::of)
                .toList();
    }
    public ResponseEntity<String> registerReview(ReviewRegisterDTO dto) {
        Customer customer = customerRepository.getReferenceById(dto.reviewerId());
        Clinic clinic = clinicRepository.getReferenceById(dto.reviewedClinicId());
        AppointmentCalendar reservation = appointmentCalendarRepository.getReferenceById(dto.reviewedAppointmentId());

        Review review = Review.of(customer, clinic, reservation, dto);

        reviewRepository.save(review);
        return ResponseEntity.ok("Review" + SUCCESSFUL_REGISTER_RESPONSE_CONTENT);
    }

    public ResponseEntity<String> deleteReviewById(DeleteDTO dto) {
        Review review = reviewRepository.getReferenceById(dto.targetId());
        if(dto.userId() == review.getReviewer().getId()) {
            reviewRepository.deleteById(dto.targetId());
            return ResponseEntity.ok("Review" +  DELETE_RESPONSE_CONTENT);
        }
        return ResponseEntity.badRequest().body(INVALID_REQUEST_RESPONSE_CONTENT + " delete review");
    }

    public Double getRatingByClinic(Long id) {
        return roundRating(reviewRepository.getRatingByClinic(id));
    }

    private double roundRating(Double number){
        if(number == null) return WITHOUT_REVIEW_RATING_VALUE;
        DecimalFormat df = new DecimalFormat("#.#");
        String formattedNumber = df.format(number);
        return Double.parseDouble(formattedNumber);
    }
}
