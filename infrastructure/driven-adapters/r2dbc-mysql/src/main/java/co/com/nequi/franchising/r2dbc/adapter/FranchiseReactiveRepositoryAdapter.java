package co.com.nequi.franchising.r2dbc.adapter;

import co.com.nequi.franchising.model.franchise.Franchise;
import co.com.nequi.franchising.model.franchise.gateways.FranchiseRepository;
import co.com.nequi.franchising.r2dbc.data.FranchiseData;
import co.com.nequi.franchising.r2dbc.helper.ReactiveAdapterOperations;
import co.com.nequi.franchising.r2dbc.repository.FranchiseReactiveRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class FranchiseReactiveRepositoryAdapter extends ReactiveAdapterOperations<
    Franchise,
    FranchiseData,
    Long,
    FranchiseReactiveRepository
> implements FranchiseRepository {
    public FranchiseReactiveRepositoryAdapter(FranchiseReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Franchise.class));
    }

}
