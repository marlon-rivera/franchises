package co.com.nequi.franchising.usecase.product;

import co.com.nequi.franchising.model.branch.Branch;
import co.com.nequi.franchising.model.branch.gateways.BranchRepository;
import co.com.nequi.franchising.model.branchproduct.BranchProduct;
import co.com.nequi.franchising.model.branchproduct.gateways.BranchProductRepository;
import co.com.nequi.franchising.model.exceptions.BranchNotExistException;
import co.com.nequi.franchising.model.exceptions.ProductCreationException;
import co.com.nequi.franchising.model.product.Product;
import co.com.nequi.franchising.model.product.gateways.ProductRepository;
import co.com.nequi.franchising.usecase.dto.ProductDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductUseCaseTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    BranchRepository branchRepository;

    @Mock
    BranchProductRepository branchProductRepository;

    @InjectMocks
    ProductUseCase productUseCase;

    @Test
    void shouldSaveProductSuccessfully() {
        Product product = new Product(1L, "Test Product");
        Integer quantity = 10;
        Long branchId = 1L;

        when(branchRepository.findById(branchId)).thenReturn(Mono.just(new Branch(branchId, "Test Branch", 1L)));
        when(productRepository.save(product)).thenReturn(Mono.just(product));
        when(branchProductRepository.save(any(BranchProduct.class))).thenReturn(Mono.just(new BranchProduct(1L, branchId, product.getId(), quantity)));

        StepVerifier.create(productUseCase.saveProduct(product, quantity, branchId))
                .expectNext(new ProductDto(product.getId(), product.getName(), quantity, branchId))
                .verifyComplete();
    }

    @Test
    void shouldReturnErrorWhenProductNameIsEmpty() {
        Product product = new Product(1L, "");
        Integer quantity = 10;
        Long branchId = 1L;

        ProductCreationException exception = assertThrows(
                ProductCreationException.class,
                () -> productUseCase.saveProduct(product, quantity, branchId)
        );

        assertEquals("The product name must not be null or empty.", exception.getMessage());
    }

    @Test
    void shouldReturnErrorWhenProductIsNull() {
        Product product = null;
        Integer quantity = 10;
        Long branchId = 1L;

        ProductCreationException exception = assertThrows(
                ProductCreationException.class,
                () -> productUseCase.saveProduct(product, quantity, branchId)
        );

        assertEquals("The product name must not be null or empty.", exception.getMessage());
    }

    @Test
    void shouldReturnErrorWhenProductNameIsNull() {
        Product product = new Product(1L, null);
        Integer quantity = 10;
        Long branchId = 1L;

        ProductCreationException exception = assertThrows(
                ProductCreationException.class,
                () -> productUseCase.saveProduct(product, quantity, branchId)
        );

        assertEquals("The product name must not be null or empty.", exception.getMessage());
    }

    @Test
    void shouldReturnErrorWhenQuantityIsNull() {
        Product product = new Product(1L, "Test Product");
        Integer quantity = null;
        Long branchId = 1L;

        ProductCreationException exception = assertThrows(
                ProductCreationException.class,
                () -> productUseCase.saveProduct(product, quantity, branchId)
        );

        assertEquals("The product quantity must not be null.", exception.getMessage());
    }

    @Test
    void shouldReturnErrorWhenQuantityIsNegative() {
        Product product = new Product(1L, "Test Product");
        Integer quantity = -5;
        Long branchId = 1L;

        ProductCreationException exception = assertThrows(
                ProductCreationException.class,
                () -> productUseCase.saveProduct(product, quantity, branchId)
        );

        assertEquals("The product quantity must be greater than zero.", exception.getMessage());
    }

    @Test
    void shouldReturnErrorWhenBranchIdIsNull() {
        Product product = new Product(1L, "Test Product");
        Integer quantity = 10;
        Long branchId = null;

        ProductCreationException exception = assertThrows(
                ProductCreationException.class,
                () -> productUseCase.saveProduct(product, quantity, branchId)
        );

        assertEquals("The product branch ID must not be null.", exception.getMessage());
    }

    @Test
    void shouldReturnErrorWhenBranchDoesNotExist() {
        Product product = new Product(1L, "Test Product");
        Integer quantity = 10;
        Long branchId = 999L;

        when(branchRepository.findById(branchId)).thenReturn(Mono.empty());

        StepVerifier.create(productUseCase.saveProduct(product, quantity, branchId))
                .expectErrorMatches(throwable -> throwable instanceof BranchNotExistException &&
                        throwable.getMessage().equals("Branch does not exist for ID: " + branchId))
                .verify();
    }

}