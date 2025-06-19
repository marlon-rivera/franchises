package co.com.nequi.franchising.api.router;

import co.com.nequi.franchising.api.handler.BranchHandler;
import co.com.nequi.franchising.utils.constants.BranchConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class BranchRouterRest {

    @Bean
    public RouterFunction<ServerResponse> branchRoutes(BranchHandler handler) {
        return route(POST(BranchConstants.ENDPOINT_CREATE_BRANCH), handler::saveBranch);
    }

}
