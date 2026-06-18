package co.com.nequi.franchising.api.handler;

import co.com.nequi.franchising.api.dto.request.ProductRequestDto;
import co.com.nequi.franchising.api.dto.request.ProductUpdateNameDto;
import co.com.nequi.franchising.api.dto.request.ProductUpdateStockRequestDto;
import co.com.nequi.franchising.api.exception.GlobalErrorWebExceptionHandler;
import co.com.nequi.franchising.model.branchproduct.BranchProduct;
import co.com.nequi.franchising.model.exceptions.InvalidDataException;
import co.com.nequi.franchising.model.exceptions.ProductCreationException;
import co.com.nequi.franchising.model.product.Product;
import co.com.nequi.franchising.usecase.dto.ProductDto;
import co.com.nequi.franchising.usecase.product.ProductUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.handler.ExceptionHandlingWebHandler;
import reactor.core.publisher.Mono;

import java.util.List;

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

        RouterFunction<ServerResponse> routerFunction = RouterFunctions.route()
                .POST("/api/product/create", handler::saveProduct).build()
                .andRoute(DELETE("/api/product/delete/product/{productId}/branch/{branchId}"), handler::deleteProductFromBranch)
                .andRoute(PUT("/api/product/update/stock"), handler::updateStockProduct)
                .andRoute(PUT("/api/product/{productId}/update-name"), handler::updateProductName);

        GlobalErrorWebExceptionHandler exceptionHandler = new GlobalErrorWebExceptionHandler(new ObjectMapper());

        ExceptionHandlingWebHandler webHandler = new ExceptionHandlingWebHandler(
                RouterFunctions.toWebHandler(routerFunction),
                List.of(exceptionHandler)
        );

        webTestClient = WebTestClient.bindToWebHandler(webHandler).build();
    }

    @Test
    void shouldReturnOkWhenProductIsValid() {
        ProductRequestDto requestDto = new ProductRequestDto("Product Name", 1L, 10);
        ProductDto productDto = new ProductDto(1L, "Product Name", 10, 1L);

        when(productUseCase.saveProduct(any(), anyInt(), anyLong()))
                .thenReturn(Mono.just(productDto));

        webTestClient.post()
                .uri("/api/product/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ProductDto.class)
                .value(response -> {
                    assertEquals(1L, response.id());
                    assertEquals("Product Name", response.name());
                });
    }

    @Test
    void shouldReturnBadRequestWhenProductNameIsEmpty() {
        when(productUseCase.saveProduct(any(), anyInt(), anyLong()))
                .thenReturn(Mono.error(new ProductCreationException("The product name must not be null or empty.")));

        ProductRequestDto requestDto = new ProductRequestDto("", 1L, 10);

        webTestClient.post()
                .uri("/api/product/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldReturnBadRequestWhenProductNameIsNull() {
        when(productUseCase.saveProduct(any(), anyInt(), anyLong()))
                .thenReturn(Mono.error(new ProductCreationException("The product name must not be null or empty.")));

        ProductRequestDto requestDto = new ProductRequestDto(null, 1L, 10);

        webTestClient.post()
                .uri("/api/product/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldReturnBadRequestWhenBranchIdIsNull() {
        when(productUseCase.saveProduct(any(), anyInt(), any()))
                .thenReturn(Mono.error(new ProductCreationException("The product branch ID must not be null.")));

        ProductRequestDto requestDto = new ProductRequestDto("Product Name", null, 10);

        webTestClient.post()
                .uri("/api/product/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldReturnBadRequestWhenInitialQuantityIsNull() {
        when(productUseCase.saveProduct(any(), any(), anyLong()))
                .thenReturn(Mono.error(new ProductCreationException("The product quantity must not be null.")));

        ProductRequestDto requestDto = new ProductRequestDto("Product Name", 1L, null);

        webTestClient.post()
                .uri("/api/product/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldReturnBadRequestWhenInitialQuantityIsNegative() {
        when(productUseCase.saveProduct(any(), anyInt(), anyLong()))
                .thenReturn(Mono.error(new ProductCreationException("The product quantity must be greater than zero.")));

        ProductRequestDto requestDto = new ProductRequestDto("Product Name", 1L, -5);

        webTestClient.post()
                .uri("/api/product/create")
                .contentType(MediaType.APPLICATION_JSON)
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
                .contentType(MediaType.APPLICATION_JSON)
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
        when(productUseCase.updateStock(any(), any(), anyInt()))
                .thenReturn(Mono.error(new ProductCreationException("The product ID or branch ID must not be null.")));

        ProductUpdateStockRequestDto requestDto = new ProductUpdateStockRequestDto(null, 1L, 20);

        webTestClient.put()
                .uri("/api/product/update/stock")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldReturnBadRequestWhenUpdateStockQuantityIsNegative() {
        when(productUseCase.updateStock(anyLong(), anyLong(), anyInt()))
                .thenReturn(Mono.error(new ProductCreationException("The product quantity must be greater than zero.")));

        ProductUpdateStockRequestDto requestDto = new ProductUpdateStockRequestDto(1L, 1L, -10);

        webTestClient.put()
                .uri("/api/product/update/stock")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldReturnBadRequestWhenUpdateStockBranchIdIsNull() {
        when(productUseCase.updateStock(anyLong(), any(), anyInt()))
                .thenReturn(Mono.error(new ProductCreationException("The product ID or branch ID must not be null.")));

        ProductUpdateStockRequestDto requestDto = new ProductUpdateStockRequestDto(1L, null, 10);

        webTestClient.put()
                .uri("/api/product/update/stock")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldReturnBadRequestWhenUpdateStockQuantityIsNull() {
        when(productUseCase.updateStock(anyLong(), anyLong(), any()))
                .thenReturn(Mono.error(new ProductCreationException("The product quantity must not be null.")));

        ProductUpdateStockRequestDto requestDto = new ProductUpdateStockRequestDto(1L, 1L, null);

        webTestClient.put()
                .uri("/api/product/update/stock")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldUpdateProductName() {
        Long productId = 1L;
        String newName = "Updated Product Name";
        ProductUpdateNameDto requestDto = new ProductUpdateNameDto(newName);

        when(productUseCase.updateProductName(productId, newName))
                .thenReturn(Mono.just(new Product(productId, newName)));

        webTestClient.put()
                .uri("/api/product/{productId}/update-name", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Product.class)
                .value(response -> {
                    assertEquals(productId, response.getId());
                    assertEquals(newName, response.getName());
                });
    }

    @Test
    void shouldReturnBadRequestWhenUpdateProductNameRequestIsInvalid() {
        Long productId = 1L;

        when(productUseCase.updateProductName(anyLong(), any()))
                .thenReturn(Mono.error(new InvalidDataException("The product name must not be null or empty.")));

        ProductUpdateNameDto requestDto = new ProductUpdateNameDto(null);

        webTestClient.put()
                .uri("/api/product/{productId}/update-name", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldReturnBadRequestWhenUpdateProductNameWithEmptyName() {
        Long productId = 1L;

        when(productUseCase.updateProductName(anyLong(), any()))
                .thenReturn(Mono.error(new InvalidDataException("The product name must not be null or empty.")));

        ProductUpdateNameDto requestDto = new ProductUpdateNameDto("");

        webTestClient.put()
                .uri("/api/product/{productId}/update-name", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldReturnBadRequestWhenInvalidProductId() {
        String invalidProductId = "invalid";

        webTestClient.put()
                .uri("/api/product/{productId}/update-name", invalidProductId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ProductUpdateNameDto("New Name"))
                .exchange()
                .expectStatus().isBadRequest();
    }

}