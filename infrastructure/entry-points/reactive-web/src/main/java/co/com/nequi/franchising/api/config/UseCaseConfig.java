package co.com.nequi.franchising.api.config;

import co.com.nequi.franchising.model.branch.gateways.BranchRepository;
import co.com.nequi.franchising.model.franchise.gateways.FranchiseRepository;
import co.com.nequi.franchising.usecase.branch.BranchUseCase;
import co.com.nequi.franchising.usecase.franchise.FranchiseUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public FranchiseUseCase franchiseUseCase(FranchiseRepository franchiseRepository) {
        return new FranchiseUseCase(franchiseRepository);
    }

    @Bean
    public BranchUseCase branchUseCase(BranchRepository branchRepository, FranchiseRepository franchiseRepository) {
        return new BranchUseCase(branchRepository, franchiseRepository);
    }

}
