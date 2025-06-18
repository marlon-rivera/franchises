package co.com.nequi.franchising.usecase.franchise;

import co.com.nequi.franchising.model.exceptions.FranchiseCreationException;
import co.com.nequi.franchising.model.franchise.Franchise;
import co.com.nequi.franchising.model.franchise.gateways.FranchiseRepository;

import co.com.nequi.franchising.usecase.utils.FranchiseMessagesConstants;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FranchiseUseCase {

    private final FranchiseRepository franchiseRepository;

    public Mono<Franchise> saveFranchise(Franchise franchise) {
        if(franchise == null || franchise.getName() == null || franchise.getName().isBlank()) {
            return Mono.error(new FranchiseCreationException(FranchiseMessagesConstants.ERROR_FRANCHISE_NAME_NOT_NULL_OR_EMPTY));
        }
        return franchiseRepository.save(franchise);
    }

}
