package co.com.nequi.franchising.r2dbc.repository;

import co.com.nequi.franchising.r2dbc.data.FranchiseData;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface FranchiseReactiveRepository extends ReactiveCrudRepository<FranchiseData, Long>, ReactiveQueryByExampleExecutor<FranchiseData> {

}
