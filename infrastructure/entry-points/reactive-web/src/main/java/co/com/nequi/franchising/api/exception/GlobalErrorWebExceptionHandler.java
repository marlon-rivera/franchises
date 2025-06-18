package co.com.nequi.franchising.api.exception;

import co.com.nequi.franchising.model.exceptions.FranchiseCreationException;
import co.com.nequi.franchising.utils.constants.FranchiseConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class GlobalErrorWebExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = ex.getMessage();
        String path = exchange.getRequest().getPath().toString();

        if (ex instanceof FranchiseCreationException) {
            status = HttpStatus.BAD_REQUEST;
            message = ex.getMessage();
            path = FranchiseConstants.ENDPOINT_CREATE_FRANCHISE;
        }

        ExceptionResponse errorResponse = new ExceptionResponse(
                LocalDateTime.now().toString(),
                status.value(),
                message,
                path
        );

        byte[] bytes;
        try {
            bytes = objectMapper.writeValueAsBytes(errorResponse);
        } catch (Exception e) {
            String fallback = "{\"status\":500,\"message\":\"Internal error\",\"path\":\"" + path + "\"}";
            bytes = fallback.getBytes(StandardCharsets.UTF_8);
        }

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        return exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
    }
}
