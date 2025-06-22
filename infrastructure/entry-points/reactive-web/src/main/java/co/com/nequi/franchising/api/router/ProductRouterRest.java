package co.com.nequi.franchising.api.router;

import co.com.nequi.franchising.api.handler.ProductHandler;
import co.com.nequi.franchising.utils.constants.ProductConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ProductRouterRest {

    @Bean
    public RouterFunction<ServerResponse> productRoutes(ProductHandler handler) {
        return route(POST(ProductConstants.ENDPOINT_CREATE_PRODUCT), handler::saveProduct);
    }

}
