package co.com.nequi.franchising.r2dbc.adapter;

import co.com.nequi.franchising.model.branch.Branch;
import co.com.nequi.franchising.model.branch.gateways.BranchRepository;
import co.com.nequi.franchising.r2dbc.data.BranchData;
import co.com.nequi.franchising.r2dbc.helper.ReactiveAdapterOperations;
import co.com.nequi.franchising.r2dbc.repository.BranchReactiveRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public class BranchReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Branch,
        BranchData,
        Long,
        BranchReactiveRepository
        > implements BranchRepository {
    public BranchReactiveRepositoryAdapter(BranchReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Branch.class));
    }

    @Override
    public Flux<Branch> findByFranchiseId(Long franchiseId) {
        return repository.findByFranchiseId(franchiseId)
                .map(data -> mapper.map(data, Branch.class));
    }
}
