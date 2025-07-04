package co.com.nequi.franchising.usecase.branch;

import co.com.nequi.franchising.model.branch.Branch;
import co.com.nequi.franchising.model.branch.gateways.BranchRepository;
import co.com.nequi.franchising.model.franchise.Franchise;
import co.com.nequi.franchising.model.franchise.gateways.FranchiseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BranchUseCaseTest {

    @Mock
    FranchiseRepository franchiseRepository;

    @Mock
    BranchRepository branchRepository;

    @InjectMocks
    BranchUseCase branchUseCase;

    @Test
    void shouldSaveBranchSuccessfully() {
        Branch input = Branch.builder().name("Test Branch").franchiseId(1L).build();
        when(franchiseRepository.findById(input.getFranchiseId())).thenReturn(Mono.just(Franchise.builder().build()));
        when(branchRepository.save(input)).thenReturn(Mono.just(input));

        StepVerifier.create(branchUseCase.saveBranch(input))
                .expectNext(input)
                .verifyComplete();
    }

    @Test
    void shouldReturnErrorWhenBranchNameIsEmpty() {
        Branch input = Branch.builder().name("").franchiseId(1L).build();

        StepVerifier.create(branchUseCase.saveBranch(input))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException && throwable.getMessage().equals("Branch name must not be null or empty"))
                .verify();
    }

    @Test
    void shouldReturnErrorWhenBranchIsNull() {
        Branch input = null;

        StepVerifier.create(branchUseCase.saveBranch(input))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException && throwable.getMessage().equals("Branch name must not be null or empty"))
                .verify();
    }

    @Test
    void shouldReturnErrorWhenBranchNameIsNull() {
        Branch input = Branch.builder().name(null).franchiseId(1L).build();

        StepVerifier.create(branchUseCase.saveBranch(input))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException && throwable.getMessage().equals("Branch name must not be null or empty"))
                .verify();
    }

    @Test
    void shouldReturnErrorWhenFranchiseIdIsNull() {
        Branch input = Branch.builder().name("Test Branch").franchiseId(null).build();

        StepVerifier.create(branchUseCase.saveBranch(input))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException && throwable.getMessage().equals("Branch franchise ID must not be null"))
                .verify();
    }

    @Test
    void shouldReturnErrorWhenFranchiseDoesNotExist() {
        Branch input = Branch.builder().name("Test Branch").franchiseId(999L).build();
        when(franchiseRepository.findById(input.getFranchiseId())).thenReturn(Mono.empty());
        StepVerifier.create(branchUseCase.saveBranch(input))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException && throwable.getMessage().equals("Franchise does not exist for ID: " + input.getFranchiseId()))
                .verify();
    }

    @Test
    void shouldUpdateBranchNameSuccessfully() {
        Long branchId = 1L;
        String newName = "Updated Branch Name";
        Branch existingBranch = Branch.builder().id(branchId).name("Old Name").franchiseId(1L).build();

        when(branchRepository.findById(branchId)).thenReturn(Mono.just(existingBranch));
        when(branchRepository.save(existingBranch)).thenReturn(Mono.just(existingBranch.toBuilder().name(newName).build()));

        StepVerifier.create(branchUseCase.updateBranchName(branchId, newName))
                .expectNextMatches(updatedBranch -> updatedBranch.getId().equals(branchId) && updatedBranch.getName().equals(newName))
                .verifyComplete();
    }

    @Test
    void shouldReturnErrorWhenUpdatingBranchNameWithEmptyName() {
        Long branchId = 1L;
        String newName = "";

        StepVerifier.create(branchUseCase.updateBranchName(branchId, newName))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException && throwable.getMessage().equals("Branch name must not be null or empty"))
                .verify();
    }

    @Test
    void shouldReturnErrorWhenUpdatingBranchNameWithNullName() {
        Long branchId = 1L;
        String newName = null;

        StepVerifier.create(branchUseCase.updateBranchName(branchId, newName))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException && throwable.getMessage().equals("Branch name must not be null or empty"))
                .verify();
    }

    @Test
    void shouldReturnErrorWhenBranchDoesNotExistForUpdate() {
        Long branchId = 999L;
        String newName = "Updated Branch Name";

        when(branchRepository.findById(branchId)).thenReturn(Mono.empty());

        StepVerifier.create(branchUseCase.updateBranchName(branchId, newName))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException && throwable.getMessage().equals("Branch does not exist for ID: " + branchId))
                .verify();
    }
}