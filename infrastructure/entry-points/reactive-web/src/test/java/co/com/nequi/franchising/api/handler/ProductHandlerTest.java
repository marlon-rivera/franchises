package co.com.nequi.franchising.api.handler;

import co.com.nequi.franchising.api.dto.request.ProductRequestDto;
import co.com.nequi.franchising.api.dto.request.ProductUpdateStockRequestDto;
import co.com.nequi.franchising.model.branchproduct.BranchProduct;
import co.com.nequi.franchising.model.product.Product;
import co.com.nequi.franchising.usecase.dto.ProductDto;
import co.com.nequi.franchising.usecase.product.ProductUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;

class ProductHandlerTest {

    private ProductUseCase productUseCase;
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        productUseCase = org.mockito.Mockito.mock(ProductUseCase.class);
        ProductHandler handler = new ProductHandler(productUseCase);

        webTestClient = WebTestClient.bindToRouterFunction(
                org.springframework.web.reactive.function.server.RouterFunctions.route()
                        .POST("/api/product/create", handler::saveProduct).build()
                        .andRoute(DELETE("/api/product/delete/product/{productId}/branch/{branchId}"), handler::deleteProductFromBranch)
                        .andRoute(PUT("/api/product/update/stock"), handler::updateStockProduct)
        ).build();
    }

    @Test
    void shouldReturnOkWhenProductIsValid() {
        ProductRequestDto requestDto = new ProductRequestDto("Product Name", 1L, 10);
        ProductDto productDto = new ProductDto(1L, "Product Name", 10, 1L);

        when(productUseCase.saveProduct(any(), anyInt(), anyLong()))
                .thenReturn(Mono.just(productDto));

        webTestClient.post()
                .uri("/api/product/create")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Product.class)
                .value(response -> {
                    assertEquals(1L, response.getId());
                    assertEquals("Product Name", response.getName());
                });
    }

    @Test
    void shouldReturnBadRequestWhenProductNameIsEmpty() {
        ProductRequestDto requestDto = new ProductRequestDto("", 1L, 10);

        webTestClient.post()
                .uri("/api/product/create")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldReturnBadRequestWhenProductNameIsNull() {
        ProductRequestDto requestDto = new ProductRequestDto(null, 1L, 10);

        webTestClient.post()
                .uri("/api/product/create")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldReturnBadRequestWhenBranchIdIsNull() {
        ProductRequestDto requestDto = new ProductRequestDto("Product Name", null, 10);

        webTestClient.post()
                .uri("/api/product/create")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldReturnBadRequestWhenInitialQuantityIsNull() {
        ProductRequestDto requestDto = new ProductRequestDto("Product Name", 1L, null);

        webTestClient.post()
                .uri("/api/product/create")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldReturnBadRequestWhenInitialQuantityIsNegative() {
        ProductRequestDto requestDto = new ProductRequestDto("Product Name", 1L, -5);

        webTestClient.post()
                .uri("/api/product/create")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldDeleteProductFromBranch() {
        Long productId = 1L;
        Long branchId = 1L;

        when(productUseCase.deleteProductFromBranch(productId, branchId))
                .thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/product/delete/product/{productId}/branch/{branchId}", productId, branchId)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void shouldReturnBadRequestWhenProductIdOrBranchIdIsNotValid() {
        webTestClient.delete()
                .uri("/api/product/delete/product/invalid/branch/invalid")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldUpdateStockProduct() {
        ProductUpdateStockRequestDto requestDto = new ProductUpdateStockRequestDto(1L, 1L, 20);
        BranchProduct branchProduct = new BranchProduct(1L, 1L, 1L, 20);
        when(productUseCase.updateStock(any(), anyLong(), anyInt()))
                .thenReturn(Mono.just(branchProduct));

        webTestClient.put()
                .uri("/api/product/update/stock")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BranchProduct.class)
                .value(response -> {
                    assertEquals(1L, response.getProductId());
                    assertEquals(1L, response.getBranchId());
                    assertEquals(20, response.getStock());
                });
    }

     @Test
    void shouldReturnBadRequestWhenUpdateStockRequestIsInvalid() {
         ProductUpdateStockRequestDto requestDto = new ProductUpdateStockRequestDto(null, 1L, 20);

         webTestClient.put()
                 .uri("/api/product/update/stock")
                 .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                 .bodyValue(requestDto)
                 .exchange()
                 .expectStatus().isBadRequest();
     }

    @Test
    void shouldReturnBadRequestWhenUpdateStockQuantityIsNegative() {
        ProductUpdateStockRequestDto requestDto = new ProductUpdateStockRequestDto(1L, 1L, -10);

        webTestClient.put()
                .uri("/api/product/update/stock")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldReturnBadRequestWhenUpdateStockBranchIdIsNull() {
        ProductUpdateStockRequestDto requestDto = new ProductUpdateStockRequestDto(1L, null, 10);

        webTestClient.put()
                .uri("/api/product/update/stock")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldReturnBadRequestWhenUpdateStockQuantityIsNull() {
        ProductUpdateStockRequestDto requestDto = new ProductUpdateStockRequestDto(1L, 1L, null);

        webTestClient.put()
                .uri("/api/product/update/stock")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isBadRequest();
    }

}