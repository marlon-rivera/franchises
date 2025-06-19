package co.com.nequi.franchising.r2dbc.repository;

import co.com.nequi.franchising.r2dbc.data.BranchData;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BranchReactiveRepository extends ReactiveCrudRepository<BranchData, Long>, ReactiveQueryByExampleExecutor<BranchData> {

}
