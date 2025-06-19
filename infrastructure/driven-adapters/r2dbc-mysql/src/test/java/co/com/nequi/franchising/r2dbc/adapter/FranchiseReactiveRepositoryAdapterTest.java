package co.com.nequi.franchising.r2dbc.adapter;

import co.com.nequi.franchising.model.franchise.Franchise;
import co.com.nequi.franchising.r2dbc.data.FranchiseData;
import co.com.nequi.franchising.r2dbc.repository.FranchiseReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FranchiseReactiveRepositoryAdapterTest {

    private FranchiseReactiveRepository repository;
    private ObjectMapper mapper;
    private FranchiseReactiveRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        repository = mock(FranchiseReactiveRepository.class);
        mapper = mock(ObjectMapper.class);
        adapter = new FranchiseReactiveRepositoryAdapter(repository, mapper);
    }

    @Test
    void shouldSaveFranchiseSuccessfully() {
        Franchise franchise = Franchise.builder().name("Nequi").build();
        FranchiseData data = new FranchiseData();

        when(mapper.map(franchise, FranchiseData.class)).thenReturn(data);
        when(repository.save(data)).thenReturn(Mono.just(data));
        when(mapper.map(data, Franchise.class)).thenReturn(franchise);

        StepVerifier.create(adapter.save(franchise))
                .expectNext(franchise)
                .verifyComplete();

        verify(repository).save(data);
    }

    @Test
    void shouldFindByIdSuccessfully() {
        FranchiseData data = new FranchiseData();
        Franchise franchise = Franchise.builder().name("KFC").build();

        when(repository.findById(1L)).thenReturn(Mono.just(data));
        when(mapper.map(data, Franchise.class)).thenReturn(franchise);

        StepVerifier.create(adapter.findById(1L))
                .expectNext(franchise)
                .verifyComplete();
    }

}