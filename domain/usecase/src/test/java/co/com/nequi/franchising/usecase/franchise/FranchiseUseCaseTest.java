package co.com.nequi.franchising.usecase.franchise;

import co.com.nequi.franchising.model.exceptions.FranchiseCreationException;
import co.com.nequi.franchising.model.franchise.Franchise;
import co.com.nequi.franchising.model.franchise.gateways.FranchiseRepository;
import co.com.nequi.franchising.usecase.utils.FranchiseMessagesConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class FranchiseUseCaseTest {

    @Mock
    FranchiseRepository franchiseRepository;

    @InjectMocks
    FranchiseUseCase franchiseUseCase;

    @Test
    void shouldSaveFranchiseSuccessfully() {
        Franchise input = Franchise.builder().name("Test").build();
        Mockito.when(franchiseRepository.save(input)).thenReturn(Mono.just(input));

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

}