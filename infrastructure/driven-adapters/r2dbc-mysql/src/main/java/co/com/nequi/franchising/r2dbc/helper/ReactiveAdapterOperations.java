package co.com.nequi.franchising.r2dbc.helper;

import co.com.nequi.franchising.model.exceptions.ServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Example;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.ParameterizedType;
import java.util.function.Function;

public abstract class ReactiveAdapterOperations<E, D, I, R extends ReactiveCrudRepository<D, I> & ReactiveQueryByExampleExecutor<D>> {
    protected R repository;
    protected ObjectMapper mapper;
    private final Class<D> dataClass;
    private final Function<D, E> toEntityFn;
    private final CircuitBreaker circuitBreaker;

    @SuppressWarnings("unchecked")
    protected ReactiveAdapterOperations(R repository, ObjectMapper mapper, Function<D, E> toEntityFn,
                                        CircuitBreaker circuitBreaker) {
        this.repository = repository;
        this.mapper = mapper;
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.dataClass = (Class<D>) genericSuperclass.getActualTypeArguments()[1];
        this.toEntityFn = toEntityFn;
        this.circuitBreaker = circuitBreaker;
    }

    protected D toData(E entity) {
        return mapper.map(entity, dataClass);
    }

    protected E toEntity(D data) {
        return data != null ? toEntityFn.apply(data) : null;
    }

    /**
     * Wraps a reactive database call with the circuit breaker. When the breaker
     * is open the call is rejected with {@link CallNotPermittedException}, which
     * is translated into a functional {@link ServiceUnavailableException}.
     */
    protected <T> Mono<T> withCircuitBreaker(Mono<T> publisher) {
        return publisher
                .transformDeferred(CircuitBreakerOperator.of(circuitBreaker))
                .onErrorMap(CallNotPermittedException.class,
                        e -> new ServiceUnavailableException(PersistenceMessages.SERVICE_UNAVAILABLE));
    }

    protected <T> Flux<T> withCircuitBreaker(Flux<T> publisher) {
        return publisher
                .transformDeferred(CircuitBreakerOperator.of(circuitBreaker))
                .onErrorMap(CallNotPermittedException.class,
                        e -> new ServiceUnavailableException(PersistenceMessages.SERVICE_UNAVAILABLE));
    }

    public Mono<E> save(E entity) {
        return withCircuitBreaker(saveData(toData(entity)).map(this::toEntity));
    }

    protected Flux<E> saveAllEntities(Flux<E> entities) {
        return withCircuitBreaker(saveData(entities.map(this::toData)).map(this::toEntity));
    }

    protected Mono<D> saveData(D data) {
        return repository.save(data);
    }

    protected Flux<D> saveData(Flux<D> data) {
        return repository.saveAll(data);
    }

    public Mono<E> findById(I id) {
        return withCircuitBreaker(repository.findById(id).map(this::toEntity));
    }

    public Flux<E> findByExample(E entity) {
        return withCircuitBreaker(repository.findAll(Example.of(toData(entity)))
                .map(this::toEntity));
    }

    public Flux<E> findAll() {
        return withCircuitBreaker(repository.findAll()
                .map(this::toEntity));
    }
}
