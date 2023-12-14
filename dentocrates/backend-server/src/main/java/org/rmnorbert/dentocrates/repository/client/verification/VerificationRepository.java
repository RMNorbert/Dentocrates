package org.rmnorbert.dentocrates.repository.client.verification;

import org.rmnorbert.dentocrates.dao.verification.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface VerificationRepository extends JpaRepository<Verification, Long> {
    Optional<Verification> findByVerificationCodeAndEmail(String verificationCode, String email);
    Optional<Verification> findByVerificationCode(String verificationCode);
}
