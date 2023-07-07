package com.rmnnorbert.dentocrates.custom.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class HealthIndicator implements ReactiveHealthIndicator {
    @Override
    public Mono<Health> health() {
        return checkHealth().onErrorResume(
                ex -> Mono.just(new Health.Builder().down(ex).build())
        );
    }
    private Mono<Health> checkHealth() {
        return Mono.just(new Health.Builder().up().build());
    }
}
