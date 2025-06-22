package co.com.nequi.franchising.api;

import co.com.nequi.franchising.api.handler.BranchHandler;
import co.com.nequi.franchising.api.handler.FranchiseHandler;
import co.com.nequi.franchising.api.handler.ProductHandler;
import co.com.nequi.franchising.api.router.BranchRouterRest;
import co.com.nequi.franchising.api.router.FranchiseRouterRest;
import co.com.nequi.franchising.api.router.ProductRouterRest;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(
        classes = {FranchiseRouterRest.class,
                FranchiseHandler.class,
                BranchRouterRest.class,
                BranchHandler.class,
                ProductRouterRest.class,
                ProductHandler.class})
@WebFluxTest
class RouterRestTest {

}
