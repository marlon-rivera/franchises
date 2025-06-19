package co.com.nequi.franchising.api.handler;

import co.com.nequi.franchising.api.dto.request.FranchiseRequestDto;
import co.com.nequi.franchising.api.dto.response.FranchiseResponseDto;
import co.com.nequi.franchising.model.franchise.Franchise;
import co.com.nequi.franchising.usecase.franchise.FranchiseUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Mono;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FranchiseHandlerTest {

    private FranchiseUseCase franchiseUseCase;
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        franchiseUseCase = mock(FranchiseUseCase.class);
        FranchiseHandler handler = new FranchiseHandler(franchiseUseCase);

        webTestClient = WebTestClient.bindToRouterFunction(
                route().POST("/api/franchise/create", handler::saveFranchise).build()
        ).build();
    }

    @Test
    void shouldReturnOkWhenFranchiseIsValid() {
        Franchise franchise = Franchise.builder().id(1L).name("Nequi").build();
        when(franchiseUseCase.saveFranchise(any())).thenReturn(Mono.just(franchise));

        FranchiseRequestDto requestDto = new FranchiseRequestDto("Nequi");

        webTestClient.post()
                .uri("/api/franchise/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(FranchiseResponseDto.class)
                .value(response -> {
                    assert response.id() == 1L;
                    assert response.name().equals("Nequi");
                });
    }

    @Test
    void shouldReturnBadRequestWhenFranchiseNameIsEmpty() {
        FranchiseRequestDto requestDto = new FranchiseRequestDto("");

        webTestClient.post()
                .uri("/api/franchise/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo(400)
                .jsonPath("$.message").value(msg ->
                        ((String) msg).contains("franchise name must not be null or empty"));
    }

    @Test
    void shouldReturnBadRequestWhenFranchiseNameIsNull() {
        FranchiseRequestDto requestDto = new FranchiseRequestDto(null);

        webTestClient.post()
                .uri("/api/franchise/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo(400)
                .jsonPath("$.message").value(msg ->
                        ((String) msg).contains("franchise name must not be null or empty"));
    }


}