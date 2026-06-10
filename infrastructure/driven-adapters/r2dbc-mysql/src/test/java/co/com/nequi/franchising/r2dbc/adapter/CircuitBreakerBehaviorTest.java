package co.com.nequi.franchising.r2dbc.adapter;

import co.com.nequi.franchising.model.exceptions.ServiceUnavailableException;
import co.com.nequi.franchising.r2dbc.repository.FranchiseReactiveRepository;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Verifies the circuit breaker that protects the persistence adapters: after the
 * configured failure threshold it opens and rejects further calls with a
 * functional {@link ServiceUnavailableException} instead of leaking the
 * underlying database error.
 */
@ExtendWith(MockitoExtension.class)
class CircuitBreakerBehaviorTest {

    private FranchiseReactiveRepository repository;
    private FranchiseReactiveRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        repository = mock(FranchiseReactiveRepository.class);
        ObjectMapper mapper = mock(ObjectMapper.class);

        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(2)
                .minimumNumberOfCalls(2)
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofSeconds(10))
                .build();
        CircuitBreaker circuitBreaker = CircuitBreaker.of("test", config);

        adapter = new FranchiseReactiveRepositoryAdapter(repository, mapper, circuitBreaker);
    }

    @Test
    void shouldOpenAndRejectWithServiceUnavailableAfterRepeatedFailures() {
        when(repository.findById(1L))
                .thenReturn(Mono.error(new RuntimeException("database connection refused")));

        // First two calls fail with the original error and trip the breaker.
        StepVerifier.create(adapter.findById(1L))
                .expectError(RuntimeException.class)
                .verify();
        StepVerifier.create(adapter.findById(1L))
                .expectError(RuntimeException.class)
                .verify();

        // Breaker is now open: the next call is short-circuited and translated
        // into a functional, non-technical error.
        StepVerifier.create(adapter.findById(1L))
                .expectErrorSatisfies(error -> {
                    assertEquals(ServiceUnavailableException.class, error.getClass());
                    assertEquals(503, ((ServiceUnavailableException) error).getHttpStatus());
                })
                .verify();
    }
}
