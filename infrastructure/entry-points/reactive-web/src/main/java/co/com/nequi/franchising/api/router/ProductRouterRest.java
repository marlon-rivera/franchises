package co.com.nequi.franchising.api.router;

import co.com.nequi.franchising.api.handler.ProductHandler;
import co.com.nequi.franchising.utils.constants.ProductConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ProductRouterRest {

    @RouterOperations({
            @RouterOperation(
                    path = ProductConstants.ENDPOINT_CREATE_PRODUCT,
                    method = POST,
                    beanClass = ProductHandler.class,
                    beanMethod = "saveProduct",
                    operation = @Operation(
                            summary = "Create Product",
                            operationId = "createProduct",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Product created successfully"
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Invalid product data"
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Internal server error"
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = ProductConstants.ENDPOINT_DELETE_PRODUCT_FROM_BRANCH,
                    method = DELETE,
                    beanClass = ProductHandler.class,
                    beanMethod = "deleteProductFromBranch",
                    operation = @Operation(
                            summary = "Delete Product from Branch",
                            operationId = "deleteProductFromBranch",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Product deleted successfully"
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Invalid product or branch ID"
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Internal server error"
                                    )
                            }
                    )
            )}
    )
    @Bean
    public RouterFunction<ServerResponse> productRoutes(ProductHandler handler) {
        return route(
                POST(ProductConstants.ENDPOINT_CREATE_PRODUCT), handler::saveProduct)
                .andRoute(DELETE(ProductConstants.ENDPOINT_DELETE_PRODUCT_FROM_BRANCH), handler::deleteProductFromBranch);
    }

}
