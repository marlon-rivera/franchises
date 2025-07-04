package co.com.nequi.franchising.api.config;

import co.com.nequi.franchising.api.handler.BranchHandler;
import co.com.nequi.franchising.api.handler.FranchiseHandler;
import co.com.nequi.franchising.api.handler.ProductHandler;
import co.com.nequi.franchising.api.router.BranchRouterRest;
import co.com.nequi.franchising.api.router.FranchiseRouterRest;
import co.com.nequi.franchising.api.router.ProductRouterRest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

@ContextConfiguration(
        classes = {
                FranchiseRouterRest.class,
                FranchiseHandler.class,
                RepositoryTestConfig.class,
                BranchRouterRest.class,
                BranchHandler.class,
                ProductRouterRest.class,
                ProductHandler.class
        })
@WebFluxTest
@Import({CorsConfig.class, SecurityHeadersConfig.class, UseCaseConfig.class})
class ConfigTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void corsConfigurationShouldAllowOrigins() {
        webTestClient.post()
                .uri("/api/franchise/create")
                .bodyValue("{\"name\":\"Test Franchise\"}")
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isOk() // O ajusta si esperas otro status
                .expectHeader().valueEquals("Content-Security-Policy", "default-src 'self'; frame-ancestors 'self'; form-action 'self'")
                .expectHeader().valueEquals("Strict-Transport-Security", "max-age=31536000;")
                .expectHeader().valueEquals("X-Content-Type-Options", "nosniff")
                .expectHeader().valueEquals("Server", "")
                .expectHeader().valueEquals("Cache-Control", "no-store")
                .expectHeader().valueEquals("Pragma", "no-cache")
                .expectHeader().valueEquals("Referrer-Policy", "strict-origin-when-cross-origin");
    }

}