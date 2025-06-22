package co.com.nequi.franchising.model.product.gateways;

import co.com.nequi.franchising.model.product.Product;
import reactor.core.publisher.Mono;

public interface ProductRepository {

    Mono<Product> save(Product product);
    Mono<Product> findById(Long id);

}
