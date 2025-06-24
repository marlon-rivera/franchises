package co.com.nequi.franchising.api.router;

import co.com.nequi.franchising.api.dto.request.ProductRequestDto;
import co.com.nequi.franchising.api.dto.request.ProductUpdateNameDto;
import co.com.nequi.franchising.api.dto.request.ProductUpdateStockRequestDto;
import co.com.nequi.franchising.api.exception.ExceptionResponse;
import co.com.nequi.franchising.api.handler.ProductHandler;
import co.com.nequi.franchising.model.branchproduct.BranchProduct;
import co.com.nequi.franchising.model.product.Product;
import co.com.nequi.franchising.utils.constants.ProductConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.bind.annotation.RequestMethod.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
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
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    description = "Entry DTO to create a product",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ProductRequestDto.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Product created successfully",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = Product.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Invalid product data",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ExceptionResponse.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Internal server error",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ExceptionResponse.class)
                                            )
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
                            parameters = {
                                    @io.swagger.v3.oas.annotations.Parameter(
                                            name = "productId",
                                            description = "ID of the product to delete",
                                            required = true,
                                            in = ParameterIn.PATH
                                    ),
                                    @io.swagger.v3.oas.annotations.Parameter(
                                            name = "branchId",
                                            description = "ID of the branch from which to delete the product",
                                            required = true,
                                            in = ParameterIn.PATH
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Product deleted successfully"
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Invalid product or branch ID",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ExceptionResponse.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Internal server error",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ExceptionResponse.class)
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = ProductConstants.ENDPOINT_UPDATE_STOCK,
                    method = PUT,
                    beanClass = ProductHandler.class,
                    beanMethod = "updateStockProduct",
                    operation = @Operation(
                            summary = "Update Product Stock",
                            operationId = "updateProductStock",
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    description = "DTO containing the product ID and new stock quantity",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ProductUpdateStockRequestDto.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Product stock updated successfully",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = BranchProduct.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Invalid product data",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ExceptionResponse.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Internal server error",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ExceptionResponse.class)
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = ProductConstants.ENDPOINT_UPDATE_NAME,
                    method = PUT,
                    beanClass = ProductHandler.class,
                    beanMethod = "updateProductName",
                    operation = @Operation(
                            summary = "Update Product Name",
                            operationId = "updateProductName",
                            parameters = {
                                    @io.swagger.v3.oas.annotations.Parameter(
                                            name = "productId",
                                            description = "ID of the product to update",
                                            required = true,
                                            in = ParameterIn.PATH
                                    )
                            },
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    description = "DTO containing the new product name",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ProductUpdateNameDto.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Product name updated successfully",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = Product.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Invalid product ID or name",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ExceptionResponse.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Internal server error",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ExceptionResponse.class)
                                            )
                                    )
                            }
                    )
            )
        }
    )
    @Bean
    public RouterFunction<ServerResponse> productRoutes(ProductHandler handler) {
        return route(
                POST(ProductConstants.ENDPOINT_CREATE_PRODUCT), handler::saveProduct)
                .andRoute(DELETE(ProductConstants.ENDPOINT_DELETE_PRODUCT_FROM_BRANCH), handler::deleteProductFromBranch)
                .andRoute(PUT(ProductConstants.ENDPOINT_UPDATE_STOCK), handler::updateStockProduct)
                .andRoute(PUT(ProductConstants.ENDPOINT_UPDATE_NAME), handler::updateProductName);
    }

}
