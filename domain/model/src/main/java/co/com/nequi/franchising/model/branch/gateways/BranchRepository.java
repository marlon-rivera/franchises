package co.com.nequi.franchising.model.branch.gateways;

import co.com.nequi.franchising.model.branch.Branch;
import reactor.core.publisher.Mono;

public interface BranchRepository {

    Mono<Branch> save(Branch branch);

}
