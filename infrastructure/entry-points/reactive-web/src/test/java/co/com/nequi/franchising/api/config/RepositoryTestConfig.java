package co.com.nequi.franchising.api.config;

import co.com.nequi.franchising.model.branch.gateways.BranchRepository;
import co.com.nequi.franchising.model.franchise.gateways.FranchiseRepository;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

@TestConfiguration
public class RepositoryTestConfig  {

    @Bean
    public FranchiseRepository franchiseRepository() {
        FranchiseRepository franchiseRepository = Mockito.mock(FranchiseRepository.class);
        Mockito.when(franchiseRepository.save(Mockito.any()))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));
        return franchiseRepository;
    }

    @Bean
    public BranchRepository branchRepository() {
        BranchRepository branchRepository = Mockito.mock(BranchRepository.class);
        Mockito.when(branchRepository.save(Mockito.any()))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));
        return branchRepository;
    }

}
