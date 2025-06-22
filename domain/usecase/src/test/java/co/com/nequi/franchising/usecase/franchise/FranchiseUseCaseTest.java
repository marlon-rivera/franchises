package co.com.nequi.franchising.usecase.franchise;

import co.com.nequi.franchising.model.branch.Branch;
import co.com.nequi.franchising.model.branch.gateways.BranchRepository;
import co.com.nequi.franchising.model.branchproduct.BranchProduct;
import co.com.nequi.franchising.model.branchproduct.gateways.BranchProductRepository;
import co.com.nequi.franchising.model.exceptions.FranchiseCreationException;
import co.com.nequi.franchising.model.franchise.Franchise;
import co.com.nequi.franchising.model.franchise.gateways.FranchiseRepository;
import co.com.nequi.franchising.model.product.Product;
import co.com.nequi.franchising.model.product.gateways.ProductRepository;
import co.com.nequi.franchising.usecase.utils.FranchiseMessagesConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FranchiseUseCaseTest {

    @Mock
    FranchiseRepository franchiseRepository;

    @Mock
    BranchRepository branchRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    BranchProductRepository branchProductRepository;

    @InjectMocks
    FranchiseUseCase franchiseUseCase;

    @Test
    void shouldSaveFranchiseSuccessfully() {
        Franchise input = Franchise.builder().name("Test").build();
        when(franchiseRepository.save(input)).thenReturn(Mono.just(input));

        StepVerifier.create(franchiseUseCase.saveFranchise(input))
                .expectNext(input)
                .verifyComplete();
    }

    @Test
    void shouldReturnErrorWhenFranchiseNameIsEmpty() {
        Franchise input = Franchise.builder().name("").build();

        StepVerifier.create(franchiseUseCase.saveFranchise(input))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException && throwable.getMessage().equals(FranchiseMessagesConstants.ERROR_FRANCHISE_NAME_NOT_NULL_OR_EMPTY))
                .verify();
    }

    @Test
    void shouldReturnErrorWhenFranchiseIsNull() {
        Franchise input = null;

        StepVerifier.create(franchiseUseCase.saveFranchise(input))
                .expectErrorMatches(throwable -> throwable instanceof FranchiseCreationException && throwable.getMessage().equals(FranchiseMessagesConstants.ERROR_FRANCHISE_NAME_NOT_NULL_OR_EMPTY))
                .verify();
    }

    @Test
    void shouldReturnErrorWhenFranchiseNameIsNull() {
        Franchise input = Franchise.builder().name(null).build();

        StepVerifier.create(franchiseUseCase.saveFranchise(input))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException && throwable.getMessage().equals(FranchiseMessagesConstants.ERROR_FRANCHISE_NAME_NOT_NULL_OR_EMPTY))
                .verify();
    }

    @Test
    void shouldReturnTopStockProductsByBranch() {
        Long franchiseId = 1L;

        Franchise franchise = Franchise.builder()
                .id(franchiseId)
                .name("Test Franchise")
                .build();

        Branch branch1 = Branch.builder()
                .id(1L)
                .name("Branch 1")
                .franchiseId(franchiseId)
                .build();

        Branch branch2 = Branch.builder()
                .id(2L)
                .name("Branch 2")
                .franchiseId(franchiseId)
                .build();

        BranchProduct topProduct1 = BranchProduct.builder()
                .id(100L)
                .branchId(branch1.getId())
                .productId(10L)
                .stock(50)
                .build();

        BranchProduct topProduct2 = BranchProduct.builder()
                .id(101L)
                .branchId(branch2.getId())
                .productId(20L)
                .stock(70)
                .build();

        Product product1 = Product.builder()
                .id(10L)
                .name("Smartphone")
                .build();

        Product product2 = Product.builder()
                .id(20L)
                .name("Laptop")
                .build();

        when(franchiseRepository.findById(franchiseId))
                .thenReturn(Mono.just(franchise));

        when(branchRepository.findByFranchiseId(franchiseId))
                .thenReturn(Flux.just(branch1, branch2));

        when(branchProductRepository.findTopByBranchIdOrderByStockDesc(branch1.getId()))
                .thenReturn(Mono.just(topProduct1));

        when(branchProductRepository.findTopByBranchIdOrderByStockDesc(branch2.getId()))
                .thenReturn(Mono.just(topProduct2));

        when(productRepository.findById(10L))
                .thenReturn(Mono.just(product1));

        when(productRepository.findById(20L))
                .thenReturn(Mono.just(product2));

        StepVerifier.create(franchiseUseCase.getTopStockProductsByBranch(franchiseId))
                .expectNextMatches(dto ->
                        dto.branch().equals(branch1) &&
                                dto.product().equals(product1) &&
                                dto.stock().equals(50)
                )
                .expectNextMatches(dto ->
                        dto.branch().equals(branch2) &&
                                dto.product().equals(product2) &&
                                dto.stock().equals(70)
                )
                .verifyComplete();
    }


    @Test
    void shouldReturnErrorWhenFranchiseDoesNotExist() {
        Long franchiseId = 999L;

        when(franchiseRepository.findById(franchiseId))
                .thenReturn(Mono.empty());

        StepVerifier.create(franchiseUseCase.getTopStockProductsByBranch(franchiseId))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException && throwable.getMessage().equals(FranchiseMessagesConstants.ERROR_FRANCHISE_NOT_EXIST + franchiseId))
                .verify();
    }
}