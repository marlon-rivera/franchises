package co.com.nequi.franchising.api.exception;

import co.com.nequi.franchising.model.exceptions.FranchiseCreationException;
import co.com.nequi.franchising.utils.constants.FranchiseConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.http.server.reactive.MockServerHttpResponse;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalErrorWebExceptionHandlerTest {

    private GlobalErrorWebExceptionHandler handler;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        handler = new GlobalErrorWebExceptionHandler(objectMapper);
    }

    @Test
    void shouldHandleFranchiseCreationException() {
        FranchiseCreationException exception =
                new FranchiseCreationException("Franchise name must not be null or empty");

        ServerWebExchange exchange = MockServerWebExchange.from(
                org.springframework.mock.http.server.reactive.MockServerHttpRequest
                        .post(FranchiseConstants.ENDPOINT_CREATE_FRANCHISE)
                        .build()
        );

        Mono<Void> result = handler.handle(exchange, exception);

        StepVerifier.create(result).verifyComplete();

        MockServerHttpResponse response = (MockServerHttpResponse) exchange.getResponse();
        assertEquals(400, response.getStatusCode().value());
        assertEquals("application/json", response.getHeaders().getContentType().toString());

        String body = response.getBodyAsString().block();
        assertTrue(body.contains("Franchise name must not be null or empty"));
        assertTrue(body.contains(FranchiseConstants.ENDPOINT_CREATE_FRANCHISE));
    }

    @Test
    void shouldHandleUnexpectedExceptionWithInternalServerError() {
        RuntimeException exception = new RuntimeException("Unexpected failure");

        ServerWebExchange exchange = MockServerWebExchange.from(
                org.springframework.mock.http.server.reactive.MockServerHttpRequest
                        .get("/some/endpoint")
                        .build()
        );

        Mono<Void> result = handler.handle(exchange, exception);

        StepVerifier.create(result).verifyComplete();

        MockServerHttpResponse response = (MockServerHttpResponse) exchange.getResponse();
        assertEquals(500, response.getStatusCode().value());
        String body = response.getBodyAsString().block();
        assertTrue(body.contains("Unexpected failure"));
        assertTrue(body.contains("/some/endpoint"));
    }

    @Test
    void shouldReturnInternalErrorWhenSerializationFails() throws JsonProcessingException {
        ObjectMapper failingMapper = mock(ObjectMapper.class);
        GlobalErrorWebExceptionHandler failingHandler = new GlobalErrorWebExceptionHandler(failingMapper);

        when(failingMapper.writeValueAsBytes(any()))
                .thenThrow(new JsonProcessingException("Serialization failure") {});

        RuntimeException exception = new RuntimeException("Simulated failure");

        ServerWebExchange exchange = MockServerWebExchange.from(
                org.springframework.mock.http.server.reactive.MockServerHttpRequest
                        .get("/some/endpoint")
                        .build()
        );

        Mono<Void> result = failingHandler.handle(exchange, exception);

        StepVerifier.create(result).verifyComplete();

        MockServerHttpResponse response = (MockServerHttpResponse) exchange.getResponse();
        assertEquals(500, response.getStatusCode().value());
        String body = response.getBodyAsString().block();

        assertTrue(body.contains("Internal error"));
        assertTrue(body.contains("/some/endpoint"));
    }

}