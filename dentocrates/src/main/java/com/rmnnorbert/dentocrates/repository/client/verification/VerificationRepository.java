package com.rmnnorbert.dentocrates.repository.client.verification;

import com.rmnnorbert.dentocrates.dao.verification.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface VerificationRepository extends JpaRepository<Verification, Long> {
    @Modifying
    @Query("DELETE FROM Verification v WHERE FUNCTION('HOUR_ADD', v.registrationTime, 12) < NOW()")
    int deleteExpiredVerifications();
    Optional<Verification> findByVerificationCodeAndEmail(String verificationCode, String email);
    Optional<Verification> findByVerificationCode(String verificationCode);
}
