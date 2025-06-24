package co.com.nequi.franchising.r2dbc.adapter;

import co.com.nequi.franchising.model.product.Product;
import co.com.nequi.franchising.r2dbc.data.ProductData;
import co.com.nequi.franchising.r2dbc.repository.ProductReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductReactiveRepositoryAdapterTest {

    private ProductReactiveRepositoryAdapter adapter;
    private ObjectMapper mapper;
    private ProductReactiveRepository repository;

    @BeforeEach
    void setUp() {
        repository = mock(ProductReactiveRepository.class);
        mapper = mock(ObjectMapper.class);
        adapter = new ProductReactiveRepositoryAdapter(repository, mapper);
    }

    @Test
    void testAdapterInitialization() {
        assertNotNull(adapter);
    }

    @Test
    void shouldSaveProductSuccessfully() {
        Product product = Product.builder().name("Test Product").build();

        when(mapper.map(product, ProductData.class)).thenReturn(new ProductData());
        when(repository.save(any(ProductData.class))).thenReturn(Mono.just(new ProductData()));
        when(mapper.map(any(ProductData.class), eq(Product.class))).thenReturn(product);

        StepVerifier.create(adapter.save(product))
                .expectNext(product)
                .verifyComplete();

        verify(repository).save(any(ProductData.class));
    }

}