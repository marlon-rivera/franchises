package co.com.nequi.franchising.api.config;

import co.com.nequi.franchising.model.branch.gateways.BranchRepository;
import co.com.nequi.franchising.model.branchproduct.gateways.BranchProductRepository;
import co.com.nequi.franchising.model.franchise.gateways.FranchiseRepository;
import co.com.nequi.franchising.model.product.gateways.ProductRepository;
import co.com.nequi.franchising.usecase.branch.BranchUseCase;
import co.com.nequi.franchising.usecase.franchise.FranchiseUseCase;
import co.com.nequi.franchising.usecase.product.ProductUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public FranchiseUseCase franchiseUseCase(
            FranchiseRepository franchiseRepository,
            BranchRepository branchRepository,
            ProductRepository productRepository,
            BranchProductRepository branchProductRepository) {
        return new FranchiseUseCase(
                franchiseRepository,
                branchRepository,
                productRepository,
                branchProductRepository);
    }

    @Bean
    public BranchUseCase branchUseCase(BranchRepository branchRepository, FranchiseRepository franchiseRepository) {
        return new BranchUseCase(branchRepository, franchiseRepository);
    }

    @Bean
    public ProductUseCase productUseCase(ProductRepository productRepository, BranchRepository branchRepository, BranchProductRepository branchProductRepository) {
        return new ProductUseCase(productRepository, branchRepository, branchProductRepository);
    }

}
