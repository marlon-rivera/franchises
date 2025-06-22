package co.com.nequi.franchising.r2dbc.adapter;

import co.com.nequi.franchising.model.branchproduct.BranchProduct;
import co.com.nequi.franchising.model.branchproduct.gateways.BranchProductRepository;
import co.com.nequi.franchising.r2dbc.data.BranchProductData;
import co.com.nequi.franchising.r2dbc.helper.ReactiveAdapterOperations;
import co.com.nequi.franchising.r2dbc.repository.BranchProductReactiveRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ProductBranchRepositoryAdapter extends ReactiveAdapterOperations<
        BranchProduct,
        BranchProductData,
        Long,
        BranchProductReactiveRepository
        > implements BranchProductRepository {
    public ProductBranchRepositoryAdapter(BranchProductReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, BranchProduct.class));
    }
}
