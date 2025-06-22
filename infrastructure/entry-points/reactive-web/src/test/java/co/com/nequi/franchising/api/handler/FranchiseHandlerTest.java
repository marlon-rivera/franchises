package co.com.nequi.franchising.api.handler;

import co.com.nequi.franchising.api.dto.request.FranchiseRequestDto;
import co.com.nequi.franchising.api.dto.response.FranchiseResponseDto;
import co.com.nequi.franchising.model.branch.Branch;
import co.com.nequi.franchising.model.franchise.Franchise;
import co.com.nequi.franchising.model.product.Product;
import co.com.nequi.franchising.usecase.dto.TopProductByBranchDto;
import co.com.nequi.franchising.usecase.franchise.FranchiseUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
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
                        .andRoute(GET("/api/franchise/{franchiseId}/top-products"), handler::getTopStockProductsByBranch)
                        .andRoute(PUT("/api/franchise/{franchiseId}/update-name"), handler::updateFranchiseName)
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

    @Test
    void shouldGetTopStockProductsByBranch() {
        Long franchiseId = 1L;

        TopProductByBranchDto dto1 = new TopProductByBranchDto(
                new Branch(1L, "Branch A", franchiseId),
                new Product(1L, "Product A"),
                10
        );

        TopProductByBranchDto dto2 = new TopProductByBranchDto(
                new Branch(2L, "Branch B", franchiseId),
                new Product(2L, "Product B"),
                30
        );

        when(franchiseUseCase.getTopStockProductsByBranch(franchiseId))
                .thenReturn(Flux.just(dto1, dto2));

        webTestClient.get()
                .uri("/api/franchise/{franchiseId}/top-products", franchiseId)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json")
                .expectBodyList(TopProductByBranchDto.class)
                .hasSize(2)
                .value(list -> {
                    assertEquals("Branch A", list.get(0).branch().getName());
                    assertEquals("Product A", list.get(0).product().getName());
                    assertEquals(10, list.get(0).stock());

                    assertEquals("Branch B", list.get(1).branch().getName());
                    assertEquals("Product B", list.get(1).product().getName());
                    assertEquals(30, list.get(1).stock());
                });
    }

    @Test
    void shouldReturnBadRequestWhenFranchiseIdIsNotNumeric() {
        String invalidFranchiseId = "abc";

        webTestClient.get()
                .uri("/api/franchise/{franchiseId}/top-products", invalidFranchiseId)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo(400)
                .jsonPath("$.message").value(msg ->
                        ((String) msg).contains("The franchise ID must be numeric."));
    }

    @Test
    void shouldReturnEmptyListWhenNoTopProductsFound() {
        Long franchiseId = 1L;
        when(franchiseUseCase.getTopStockProductsByBranch(franchiseId)).thenReturn(Flux.empty());

        webTestClient.get()
                .uri("/api/franchise/{franchiseId}/top-products", franchiseId)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TopProductByBranchDto.class)
                .hasSize(0);
    }

    @Test
    void shouldReturnInternalServerErrorOnException() {
        Long franchiseId = 1L;
        when(franchiseUseCase.getTopStockProductsByBranch(franchiseId))
                .thenReturn(Flux.error(new RuntimeException("Internal error")));

        webTestClient.get()
                .uri("/api/franchise/{franchiseId}/top-products", franchiseId)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .jsonPath("$.status").isEqualTo(500)
                .jsonPath("$.message").value(msg -> ((String) msg).contains("Internal error"));
    }

    @Test
    void shouldUpdateFranchiseName() {
        Long franchiseId = 1L;
        String newName = "Updated Franchise";

        when(franchiseUseCase.updateName(franchiseId, newName)).thenReturn(Mono.just(new Franchise(franchiseId, newName)));

        webTestClient.put()
                .uri("/api/franchise/{franchiseId}/update-name", franchiseId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new FranchiseRequestDto(newName))
                .exchange()
                .expectStatus().isOk()
                .expectBody(FranchiseResponseDto.class)
                .value(response -> {
                    assertEquals(franchiseId, response.id());
                    assertEquals(newName, response.name());
                });
    }

    @Test
    void shouldReturnBadRequestWhenUpdatingFranchiseNameWithEmptyName() {
        Long franchiseId = 1L;

        webTestClient.put()
                .uri("/api/franchise/{franchiseId}/update-name", franchiseId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new FranchiseRequestDto(""))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo(400)
                .jsonPath("$.message").value(msg ->
                        ((String) msg).contains("franchise name must not be null or empty"));
    }

    @Test
    void shouldReturnBadRequestWhenUpdatingFranchiseNameWithNullName() {
        Long franchiseId = 1L;

        webTestClient.put()
                .uri("/api/franchise/{franchiseId}/update-name", franchiseId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new FranchiseRequestDto(null))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo(400)
                .jsonPath("$.message").value(msg ->
                        ((String) msg).contains("franchise name must not be null or empty"));
    }

    @Test
    void shouldReturnBadRequestWhenUpdatingFranchiseNameWithNonNumericId() {
        String invalidFranchiseId = "abc";

        webTestClient.put()
                .uri("/api/franchise/{franchiseId}/update-name", invalidFranchiseId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new FranchiseRequestDto("New Name"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo(400)
                .jsonPath("$.message").value(msg ->
                        ((String) msg).contains("The franchise ID must be numeric."));
    }

}