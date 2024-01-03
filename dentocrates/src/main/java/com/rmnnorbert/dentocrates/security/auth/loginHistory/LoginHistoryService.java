package com.rmnnorbert.dentocrates.security.auth.loginHistory;

import com.rmnnorbert.dentocrates.dao.client.Client;
import com.rmnnorbert.dentocrates.dao.loggingHistory.LoginHistory;
import com.rmnnorbert.dentocrates.repository.client.ClientRepository;
import com.rmnnorbert.dentocrates.repository.loginHistory.LoginHistoryRepository;
import com.rmnnorbert.dentocrates.security.auth.loginHistory.loginDetailsHandler.IpAddressValidator;
import com.rmnnorbert.dentocrates.security.auth.loginHistory.loginDetailsHandler.UserAgentSanitizer;
import com.rmnnorbert.dentocrates.service.client.communicationServices.LoginNotificationService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Service
public class LoginHistoryService {
    private final static int IP_ADDRESS_INDEX = 0;
    private final static int USER_AGENT_INDEX = 1;
    private final LoginHistoryRepository loginHistoryRepository;
    private final ClientRepository clientRepository;
    private final LoginNotificationService loginNotificationService;
    private final Counter loginSuccessCounter;
    private final Counter loginFailureCounter;
    private final IpAddressValidator validatorService;
    private final UserAgentSanitizer sanitizerService;

    @Autowired
    public LoginHistoryService(LoginHistoryRepository loginHistoryRepository, ClientRepository clientRepository,
                               LoginNotificationService loginNotificationService, IpAddressValidator validatorService,
                               UserAgentSanitizer sanitizerService) {

        this.loginHistoryRepository = loginHistoryRepository;
        this.clientRepository = clientRepository;
        this.loginNotificationService = loginNotificationService;
        this.validatorService = validatorService;
        this.sanitizerService = sanitizerService;
        loginSuccessCounter = Metrics.counter("counter.login.success");
        loginFailureCounter = Metrics.counter("counter.login.failure");
    }
    public void successfulLogin(String userId) {
        try {
            storeLoginAttempt(userId);
            loginSuccessCounter.increment();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void unSuccessfulLogin(String userId) {
        try{
            storeLoginAttempt(userId);
            loginFailureCounter.increment();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private String[] storeLoginAttempt(String userId) {
        Optional<Client> client = clientRepository.getClientByEmail(userId);
        if (client.isPresent()) {
            String[] loginDetails = getLoginDetails();
            String ipAddress = loginDetails[IP_ADDRESS_INDEX];
            String userAgent = loginDetails[USER_AGENT_INDEX];
            String email = client.get().getEmail();

            LoginHistory loginHistory = LoginHistory.of(email, ipAddress, userAgent);

            boolean registeredLoginDetails = isLoginDetailsRegistered(ipAddress, userAgent, email);
            if (!registeredLoginDetails) {
                loginNotificationService.sendLoginNotification(ipAddress,userAgent, email);
            }
            loginHistoryRepository.save(loginHistory);
            return new String[]{email, ipAddress, userAgent};
        }
        return new String[]{};
    }

    private String[] getLoginDetails() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();

        if (request != null) {
            String clientIpAddress = request.getRemoteAddr();
            String userAgent = request.getHeader("User-Agent");

            if (!validatorService.isValidIPAddress(clientIpAddress)) {
                clientIpAddress = "Unknown";
            }
            userAgent = sanitizerService.sanitizeUserAgent(userAgent);

            return new String[]{clientIpAddress, userAgent};
        } else {
            return new String[]{"Unknown", "Unknown"};
        }
    }
    private boolean isLoginDetailsRegistered(String ipAddress, String userAgent, String email){
        return loginHistoryRepository.existsByLoginDetails(email, ipAddress, userAgent);
    }
}
