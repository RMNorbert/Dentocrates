package org.rmnorbert.dentocrates.repository.loginHistory;

import jakarta.validation.constraints.Email;
import org.rmnorbert.dentocrates.dao.loggingHistory.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {
 @Query("SELECT COUNT(l) > 0 FROM LoginHistory l WHERE l.email = :email and l.ip_address = :ipAddress and l.user_agent = :userAgent")
 boolean existsByLoginDetails(@Email String email, String ipAddress, String userAgent);
}
