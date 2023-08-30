package com.rmnnorbert.dentocrates.repository;

import com.rmnnorbert.dentocrates.dao.verification.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface VerificationRepository extends JpaRepository<Verification, Long> {
    @Query("DELETE FROM Verification v WHERE FUNCTION('HOUR_ADD', v.registrationTime, 12) < NOW()")
    int deleteExpiredVerifications();
    Optional<Verification> findByVerificationCode(String verificationCode);
    int deleteByEmail(String email);
}
