package co.com.nequi.franchising.config;

import co.com.nequi.franchising.model.branch.gateways.BranchRepository;
import co.com.nequi.franchising.model.branchproduct.gateways.BranchProductRepository;
import co.com.nequi.franchising.model.franchise.gateways.FranchiseRepository;
import co.com.nequi.franchising.model.product.gateways.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertTrue;

class UseCasesConfigTest {

    @Test
    void testUseCaseBeansExist() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class)) {
            String[] beanNames = context.getBeanDefinitionNames();

            boolean useCaseBeanFound = false;
            for (String beanName : beanNames) {
                if (beanName.endsWith("UseCase")) {
                    useCaseBeanFound = true;
                    break;
                }
            }

            assertTrue(useCaseBeanFound, "No beans ending with 'Use Case' were found");
        }
    }

    @Configuration
    @Import(UseCasesConfig.class)
    static class TestConfig {

        @Bean
        public FranchiseRepository franchiseRepository() {
            FranchiseRepository mock = Mockito.mock(FranchiseRepository.class);
            Mockito.when(mock.save(Mockito.any()))
                    .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));
            return mock;
        }

        @Bean
        public BranchRepository branchRepository() {
            BranchRepository mock = Mockito.mock(BranchRepository.class);
            Mockito.when(mock.save(Mockito.any()))
                    .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));
            return mock;
        }

        @Bean
        public ProductRepository productRepository() {
            ProductRepository mock = Mockito.mock(ProductRepository.class);
            Mockito.when(mock.save(Mockito.any()))
                    .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));
            return mock;
        }

        @Bean
        public BranchProductRepository branchProductRepository() {
            BranchProductRepository mock = Mockito.mock(BranchProductRepository.class);
            Mockito.when(mock.save(Mockito.any()))
                    .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));
            return mock;
        }
    }
}