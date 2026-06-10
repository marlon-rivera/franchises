package co.com.nequi.franchising.r2dbc.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Exposes the database circuit breaker as a bean so the persistence adapters can
 * decorate their reactive calls with it. The actual thresholds are read from the
 * {@code resilience4j.circuitbreaker.instances.franchiseDb} properties.
 */
@Configuration
public class Resilience4jConfig {

    public static final String FRANCHISE_DB_CIRCUIT_BREAKER = "franchiseDb";

    @Bean
    public CircuitBreaker franchiseDbCircuitBreaker(CircuitBreakerRegistry registry) {
        return registry.circuitBreaker(FRANCHISE_DB_CIRCUIT_BREAKER);
    }
}
