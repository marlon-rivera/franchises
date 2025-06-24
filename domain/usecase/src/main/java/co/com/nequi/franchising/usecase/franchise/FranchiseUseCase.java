package co.com.nequi.franchising.usecase.franchise;

import co.com.nequi.franchising.model.branch.gateways.BranchRepository;
import co.com.nequi.franchising.model.branchproduct.gateways.BranchProductRepository;
import co.com.nequi.franchising.model.exceptions.FranchiseCreationException;
import co.com.nequi.franchising.model.exceptions.FranchiseNotExistException;
import co.com.nequi.franchising.model.exceptions.InvalidDataException;
import co.com.nequi.franchising.model.franchise.Franchise;
import co.com.nequi.franchising.model.franchise.gateways.FranchiseRepository;

import co.com.nequi.franchising.model.product.gateways.ProductRepository;
import co.com.nequi.franchising.usecase.dto.TopProductByBranchDto;
import co.com.nequi.franchising.usecase.utils.FranchiseMessagesConstants;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FranchiseUseCase {

    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;
    private final BranchProductRepository branchProductRepository;

    public Mono<Franchise> saveFranchise(Franchise franchise) {
        if(franchise == null || franchise.getName() == null || franchise.getName().isBlank()) {
            return Mono.error(new FranchiseCreationException(FranchiseMessagesConstants.ERROR_FRANCHISE_NAME_NOT_NULL_OR_EMPTY));
        }
        return franchiseRepository.save(franchise);
    }

    public Flux<TopProductByBranchDto> getTopStockProductsByBranch(Long franchiseId) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotExistException(
                        FranchiseMessagesConstants.ERROR_FRANCHISE_NOT_EXIST + franchiseId
                )))
                .flatMapMany(franchise ->
                        branchRepository.findByFranchiseId(franchiseId))
                        .flatMap(branch ->
                                branchProductRepository.findTopByBranchIdOrderByStockDesc(branch.getId())
                                .flatMap(branchProduct ->
                                    productRepository.findById(branchProduct.getProductId())
                                        .map(product -> new TopProductByBranchDto(
                                                branch,
                                                product,
                                                branchProduct.getStock()
                                        ))
                                ));


    }

    public Mono<Franchise> updateName(Long franchiseId, String newName) {
        if (newName == null || newName.isBlank()) {
            return Mono.error(new InvalidDataException(FranchiseMessagesConstants.ERROR_FRANCHISE_NAME_NOT_NULL_OR_EMPTY));
        }
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotExistException(
                        FranchiseMessagesConstants.ERROR_FRANCHISE_NOT_EXIST + franchiseId
                )))
                .flatMap(franchise -> {
                    franchise.setName(newName);
                    return franchiseRepository.save(franchise);
                });
    }
}
