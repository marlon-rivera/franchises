package co.com.nequi.franchising.r2dbc.adapter;

import co.com.nequi.franchising.model.branch.Branch;
import co.com.nequi.franchising.r2dbc.data.BranchData;
import co.com.nequi.franchising.r2dbc.repository.BranchReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BranchReactiveRepositoryAdapterTest {

    private BranchReactiveRepository repository;
    private ObjectMapper mapper;
    private BranchReactiveRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        repository = org.mockito.Mockito.mock(BranchReactiveRepository.class);
        mapper = org.mockito.Mockito.mock(ObjectMapper.class);
        adapter = new BranchReactiveRepositoryAdapter(repository, mapper);
    }

    @Test
    void shouldSaveBranchSuccessfully() {
        Branch branch = Branch.builder().name("Test Branch").build();
        BranchData data = new BranchData();

        when(mapper.map(branch, BranchData.class)).thenReturn(data);
        when(repository.save(data)).thenReturn(Mono.just(data));
        when(mapper.map(data, Branch.class)).thenReturn(branch);

        StepVerifier.create(adapter.save(branch))
                .expectNext(branch)
                .verifyComplete();

        org.mockito.Mockito.verify(repository).save(data);
    }

    @Test
    void shouldFindByIdSuccessfully() {
        BranchData data = new BranchData();
        Branch branch = Branch.builder().name("Test Branch").build();

        when(repository.findById(1L)).thenReturn(Mono.just(data));
        when(mapper.map(data, Branch.class)).thenReturn(branch);

        StepVerifier.create(adapter.findById(1L))
                .expectNext(branch)
                .verifyComplete();
    }

    @Test
    void shouldBeEmptyWhenBranchNotFound() {
        when(repository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.findById(1L))
                .expectComplete()
                .verify();

        org.mockito.Mockito.verify(repository).findById(1L);
    }

    @Test
    void shouldFindByFranchiseIdSuccessfully() {
        BranchData data = new BranchData();
        Branch branch = Branch.builder().name("Test Branch").build();

        when(repository.findByFranchiseId(1L)).thenReturn(Flux.just(data));
        when(mapper.map(data, Branch.class)).thenReturn(branch);

        StepVerifier.create(adapter.findByFranchiseId(1L))
                .expectNext(branch)
                .verifyComplete();
    }

}