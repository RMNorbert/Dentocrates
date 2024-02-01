package com.rmnnorbert.dentocrates.scheduler;

import com.rmnnorbert.dentocrates.repository.client.ClientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DailyCleanupTaskScheduler {
    private final ClientRepository clientRepository;
    private final static int HOURS_AVAILABLE_TO_VERIFY = 24;
    @Autowired
    public DailyCleanupTaskScheduler(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Scheduled(cron = "${cron.expression}")
    @Transactional
    public void scheduleCleanupTaskUsingCronExpression() {
        cleanupUnverifiedClients();
    }

    private void cleanupUnverifiedClients() {
        clientRepository.deleteUnverifiedClients(HOURS_AVAILABLE_TO_VERIFY);
    }
}
