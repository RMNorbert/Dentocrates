package com.rmnnorbert.dentocrates.custom.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/** Implements the ReactiveHealthIndicator interface to provide health check functionality. */
@Component
public class HealthIndicator implements ReactiveHealthIndicator {
    /**
     * Performs the health check and returns a Mono emitting the health status.
     *
     * @return A Mono emitting the health status.
     */
    @Override
    public Mono<Health> health() {
        return checkHealth().onErrorResume(
                ex -> Mono.just(new Health.Builder().down(ex).build())
        );
    }
    /**
     * Simulates a health check and returns an 'up' status.
     *
     * @return A Mono emitting the 'up' health status.
     */
    private Mono<Health> checkHealth() {
        return Mono.just(new Health.Builder().up().build());
    }
}
