package co.com.nequi.franchising.r2dbc.adapter;

import co.com.nequi.franchising.model.branchproduct.BranchProduct;
import co.com.nequi.franchising.model.branchproduct.gateways.BranchProductRepository;
import co.com.nequi.franchising.r2dbc.data.BranchProductData;
import co.com.nequi.franchising.r2dbc.helper.ReactiveAdapterOperations;
import co.com.nequi.franchising.r2dbc.repository.BranchProductReactiveRepository;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class ProductBranchRepositoryAdapter extends ReactiveAdapterOperations<
        BranchProduct,
        BranchProductData,
        Long,
        BranchProductReactiveRepository
        > implements BranchProductRepository {
    public ProductBranchRepositoryAdapter(BranchProductReactiveRepository repository, ObjectMapper mapper,
                                          CircuitBreaker franchiseDbCircuitBreaker) {
        super(repository, mapper, d -> mapper.map(d, BranchProduct.class), franchiseDbCircuitBreaker);
    }

    @Override
    public Mono<BranchProduct> findByProductIdAndBranchId(Long productId, Long branchId) {
        return withCircuitBreaker(repository.findByProductIdAndBranchId(productId, branchId)
                .map(this::toEntity));
    }

    @Override
    public Mono<Void> delete(BranchProduct branchProduct) {
        return withCircuitBreaker(repository.delete(toData(branchProduct)));
    }

    @Override
    public Mono<BranchProduct> findTopByBranchIdOrderByStockDesc(Long branchId) {
        return withCircuitBreaker(repository.findTopByBranchIdOrderByStockDesc(branchId)
                .map(this::toEntity));
    }
}
