package co.com.nequi.franchising.r2dbc.repository;

import co.com.nequi.franchising.r2dbc.data.BranchProductData;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface BranchProductReactiveRepository extends ReactiveCrudRepository<BranchProductData, Long>, ReactiveQueryByExampleExecutor<BranchProductData> {

    Mono<BranchProductData> findByProductIdAndBranchId(Long productId, Long branchId);
    Mono<Void> delete(BranchProductData branchProductData);
    Mono<BranchProductData> findTopByBranchIdOrderByStockDesc(Long branchId);
}
