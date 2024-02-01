package com.rmnnorbert.dentocrates.service.review;

import com.rmnnorbert.dentocrates.custom.exceptions.NotFoundException;
import com.rmnnorbert.dentocrates.dao.client.Customer;
import com.rmnnorbert.dentocrates.dao.appointment.Appointment;
import com.rmnnorbert.dentocrates.dao.clinic.Clinic;
import com.rmnnorbert.dentocrates.dao.review.Review;
import com.rmnnorbert.dentocrates.dto.DeleteDTO;
import com.rmnnorbert.dentocrates.dto.review.ReviewDTO;
import com.rmnnorbert.dentocrates.dto.review.ReviewRegisterDTO;
import com.rmnnorbert.dentocrates.repository.clinic.appointment.AppointmentRepository;
import com.rmnnorbert.dentocrates.repository.clinic.ClinicRepository;
import com.rmnnorbert.dentocrates.repository.client.CustomerRepository;
import com.rmnnorbert.dentocrates.repository.clinic.review.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

import static com.rmnnorbert.dentocrates.controller.ApiResponseConstants.*;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final AppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;
    private final ClinicRepository clinicRepository;

    /** Default rating value used when there are no reviews available. */
    private final static Double WITHOUT_REVIEW_RATING_VALUE = 0.0;
    @Autowired
    public ReviewService(ReviewRepository reviewRepository,
                         AppointmentRepository appointmentRepository,
                         CustomerRepository customerRepository,
                         ClinicRepository clinicRepository) {
        this.reviewRepository = reviewRepository;
        this.appointmentRepository = appointmentRepository;
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
        Review review = createReviewFromDTO(dto);
        reviewRepository.save(review);
        return ResponseEntity.ok("Review" + SUCCESSFUL_REGISTER_RESPONSE_CONTENT);
    }

    public ResponseEntity<String> deleteReviewById(DeleteDTO dto) {
        Review review = findReviewById(dto.targetId());
        if(dto.userId() == review.getReviewer().getId()) {
            reviewRepository.deleteById(dto.targetId());
            return ResponseEntity.ok("Review" +  DELETE_RESPONSE_CONTENT);
        }
        return ResponseEntity.badRequest().body(INVALID_REQUEST_RESPONSE_CONTENT + " delete review");
    }

    public Double getRatingByClinic(Long id) {
        return roundRating(reviewRepository.getRatingByClinic(id));
    }

    /**
     * Rounds the given rating to one decimal place.
     * This method takes a Double value representing a rating and rounds it to one decimal place
     * using DecimalFormat. If the input rating is null, it returns a default value indicating
     * the absence of a review rating.
     *
     * @param number The rating to be rounded.
     * @return The rounded rating or a default value if the input is null.
     */
    private double roundRating(Double number){
        if(number == null) return WITHOUT_REVIEW_RATING_VALUE;
        DecimalFormat df = new DecimalFormat("#.#");
        String formattedNumber = df.format(number);
        return Double.parseDouble(formattedNumber);
    }

    /**
     * Creates a new Review object based on the information in the given ReviewRegisterDTO.
     *
     * This method ensures the consistency of the provided review data by checking if the reviewer (customer)
     * matches the customer associated with the reviewed appointment. If the match is confirmed, it retrieves
     * references to the associated Customer, Clinic, and Appointment using the provided IDs, and then
     * constructs a new Review object.
     *
     * @param dto The ReviewRegisterDTO containing the review information.
     * @return A Review object created and verified from the provided ReviewRegisterDTO.
     * @throws NotFoundException If the appointment and customer do not match, indicating an inconsistency in the data.
     */
    private Review createReviewFromDTO(ReviewRegisterDTO dto) {
        Customer customer = findCustomerById(dto.reviewerId());
        Appointment reservation = findAppointmentById(dto.reviewedAppointmentId());

        if(Objects.equals(reservation.getCustomer().getId(), customer.getId())) {
            Clinic clinic = findClinicById(dto.reviewedClinicId());
            return Review.of(customer, clinic, reservation, dto);
        } else {
            throw new NotFoundException("Appointment");
        }
    }
    private Customer findCustomerById(long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer"));
    }
    private Appointment findAppointmentById(long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Appointment"));
    }
    private Review findReviewById(long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Review"));
    }
    private Clinic findClinicById(long id) {
        return clinicRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Clinic"));
    }
}
