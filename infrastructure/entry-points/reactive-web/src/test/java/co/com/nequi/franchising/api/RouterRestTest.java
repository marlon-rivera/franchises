package co.com.nequi.franchising.api;

import co.com.nequi.franchising.api.handler.FranchiseHandler;
import co.com.nequi.franchising.api.router.FranchiseRouterRest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

@ContextConfiguration(classes = {FranchiseRouterRest.class, FranchiseHandler.class})
@WebFluxTest
class RouterRestTest {


}
