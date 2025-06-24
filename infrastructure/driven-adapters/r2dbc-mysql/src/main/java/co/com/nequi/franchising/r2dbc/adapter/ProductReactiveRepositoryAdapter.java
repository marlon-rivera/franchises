package co.com.nequi.franchising.r2dbc.adapter;

import co.com.nequi.franchising.model.product.Product;
import co.com.nequi.franchising.model.product.gateways.ProductRepository;
import co.com.nequi.franchising.r2dbc.data.ProductData;
import co.com.nequi.franchising.r2dbc.helper.ReactiveAdapterOperations;
import co.com.nequi.franchising.r2dbc.repository.ProductReactiveRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ProductReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Product,
        ProductData,
        Long,
        ProductReactiveRepository>
 implements ProductRepository{


    public ProductReactiveRepositoryAdapter(ProductReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Product.class));
    }
}
