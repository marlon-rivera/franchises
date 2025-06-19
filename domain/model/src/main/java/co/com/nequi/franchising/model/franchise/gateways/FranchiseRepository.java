package co.com.nequi.franchising.model.franchise.gateways;

import co.com.nequi.franchising.model.franchise.Franchise;
import reactor.core.publisher.Mono;

public interface FranchiseRepository {

    Mono<Franchise> save(Franchise franchise);
    Mono<Franchise> findById(Long id);

}
