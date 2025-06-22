package co.com.nequi.franchising.model.branchproduct.gateways;

import co.com.nequi.franchising.model.branchproduct.BranchProduct;
import reactor.core.publisher.Mono;

public interface BranchProductRepository {

    Mono<BranchProduct> save(BranchProduct branchProduct);
    Mono<BranchProduct> findByProductIdAndBranchId(Long productId, Long branchId);
    Mono<Void> delete(BranchProduct branchProduct);


}
