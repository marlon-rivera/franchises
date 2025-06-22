package co.com.nequi.franchising.r2dbc.adapter;

import co.com.nequi.franchising.model.branchproduct.BranchProduct;
import co.com.nequi.franchising.r2dbc.data.BranchProductData;
import co.com.nequi.franchising.r2dbc.repository.BranchProductReactiveRepository;
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
class ProductBranchRepositoryAdapterTest {

    private ProductBranchRepositoryAdapter adapter;
    private ObjectMapper mapper;
    private BranchProductReactiveRepository repository;

    @BeforeEach
    void setUp() {
        repository = mock(BranchProductReactiveRepository.class);
        mapper = mock(ObjectMapper.class);
        adapter = new ProductBranchRepositoryAdapter(repository, mapper);
    }

    @Test
    void testAdapterInitialization() {
        assertNotNull(adapter);
    }

    @Test
    void shouldSaveProductBranchSuccessfully() {
        BranchProduct productBranch = new BranchProduct(1L, 1L, 1L, 10);

        when(mapper.map(productBranch, BranchProductData.class)).thenReturn(new BranchProductData());
        when(repository.save(any(BranchProductData.class))).thenReturn(Mono.just(new BranchProductData()));
        when(mapper.map(any(BranchProductData.class), eq(BranchProduct.class))).thenReturn(productBranch);

        StepVerifier.create(adapter.save(productBranch))
                 .expectNext(productBranch)
                 .verifyComplete();

        verify(repository).save(any(BranchProductData.class));
    }

}