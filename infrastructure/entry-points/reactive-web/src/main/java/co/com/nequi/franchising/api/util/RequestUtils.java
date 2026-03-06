package co.com.nequi.franchising.api.util;

import co.com.nequi.franchising.model.exceptions.InvalidDataException;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

public class RequestUtils {

    private RequestUtils() {
    }

    public static Mono<Long> parsePathVariableToLong(ServerRequest request, String key) {
        return Mono.defer(() -> {
            try {
                return Mono.just(Long.valueOf(request.pathVariable(key)));
            } catch (NumberFormatException e) {
                return Mono.error(new InvalidDataException(
                        "The path variable '" + key + "' must be numeric."
                ));
            }
        });
    }

    public static <T> Mono<T> requireBody(ServerRequest request, Class<T> clazz) {
        return request.bodyToMono(clazz)
                .switchIfEmpty(Mono.error(new InvalidDataException("Request body is required")));
    }
}
