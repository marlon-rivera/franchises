package co.com.nequi.franchising.model.exceptions;

/**
 * Raised when a downstream dependency (e.g. the database) is not reachable and
 * the circuit breaker rejects the call. Mapped to HTTP 503 by the entry-point
 * layer so the client gets a functional "try again later" message instead of a
 * technical failure.
 */
public class ServiceUnavailableException extends BusinessException {
    public ServiceUnavailableException(String message) {
        super(message, 503);
    }
}
