package co.com.nequi.franchising.usecase.branch;

import co.com.nequi.franchising.model.branch.Branch;
import co.com.nequi.franchising.model.branch.gateways.BranchRepository;
import co.com.nequi.franchising.model.exceptions.BranchCreationException;
import co.com.nequi.franchising.model.exceptions.FranchiseNotExistException;
import co.com.nequi.franchising.model.franchise.gateways.FranchiseRepository;
import co.com.nequi.franchising.usecase.utils.BranchMessagesConstants;
import co.com.nequi.franchising.usecase.utils.FranchiseMessagesConstants;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BranchUseCase {

    private final BranchRepository branchRepository;
    private final FranchiseRepository franchiseRepository;

    public Mono<Branch> saveBranch(Branch branch) {
        if (branch == null || branch.getName() == null || branch.getName().isBlank()) {
            return Mono.error(new BranchCreationException(BranchMessagesConstants.ERROR_BRANCH_NAME_NOT_NULL_OR_EMPTY));
        }
        if(branch.getFranchiseId() == null) {
            return Mono.error(new BranchCreationException(BranchMessagesConstants.ERROR_BRANCH_FRANCHISE_ID_NOT_VALID));
        }
        return franchiseRepository.findById(branch.getFranchiseId())
                .switchIfEmpty(Mono.error(new FranchiseNotExistException(FranchiseMessagesConstants.ERROR_FRANCHISE_NOT_EXIST + branch.getFranchiseId())))
                .flatMap(franchise -> branchRepository.save(branch));
    }

    public Mono<Branch> updateBranchName(Long branchId, String newName) {
        if (newName == null || newName.isBlank()) {
            return Mono.error(new BranchCreationException(BranchMessagesConstants.ERROR_BRANCH_NAME_NOT_NULL_OR_EMPTY));
        }
        return branchRepository.findById(branchId)
                .switchIfEmpty(Mono.error(new FranchiseNotExistException(
                        FranchiseMessagesConstants.ERROR_FRANCHISE_NOT_EXIST + branchId
                )))
                .flatMap(branch -> {
                    branch.setName(newName);
                    return branchRepository.save(branch);
                });
    }

}
