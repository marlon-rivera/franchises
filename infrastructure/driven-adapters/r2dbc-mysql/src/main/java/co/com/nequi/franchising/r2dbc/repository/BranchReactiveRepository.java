package co.com.nequi.franchising.r2dbc.repository;

import co.com.nequi.franchising.r2dbc.data.BranchData;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface BranchReactiveRepository extends ReactiveCrudRepository<BranchData, Long>, ReactiveQueryByExampleExecutor<BranchData> {

    Flux<BranchData> findByFranchiseId(Long franchiseId);

}
