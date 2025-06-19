package co.com.nequi.franchising.api.config;

import co.com.nequi.franchising.model.franchise.gateways.FranchiseRepository;
import co.com.nequi.franchising.usecase.franchise.FranchiseUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public FranchiseUseCase franchiseUseCase(FranchiseRepository franchiseRepository) {
        return new FranchiseUseCase(franchiseRepository);
    }

}
