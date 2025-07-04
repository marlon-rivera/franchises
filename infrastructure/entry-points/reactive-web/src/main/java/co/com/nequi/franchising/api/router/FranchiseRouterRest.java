package co.com.nequi.franchising.api.router;

import co.com.nequi.franchising.api.dto.request.FranchiseRequestDto;
import co.com.nequi.franchising.api.dto.request.FranchiseUpdateNameDto;
import co.com.nequi.franchising.api.dto.request.ProductUpdateStockRequestDto;
import co.com.nequi.franchising.api.dto.response.FranchiseResponseDto;
import co.com.nequi.franchising.api.exception.ExceptionResponse;
import co.com.nequi.franchising.api.handler.FranchiseHandler;
import co.com.nequi.franchising.utils.constants.FranchiseConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration

public class FranchiseRouterRest {

    @RouterOperations({
            @RouterOperation(
                    path = FranchiseConstants.ENDPOINT_CREATE_FRANCHISE,
                    method = RequestMethod.POST,
                    beanClass = FranchiseHandler.class,
                    beanMethod = "saveFranchise",
                    operation = @Operation(
                            summary = "Create Franchise",
                            operationId = "createFranchise",
                            requestBody = @RequestBody(
                                    description = "Entry DTO to create a franchise",
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = FranchiseRequestDto.class))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Franchise created successfully",
                                            content = @Content(schema = @Schema(implementation = FranchiseResponseDto.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Franchise name must not be null or empty",
                                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
                                    ),

                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Internal server error",
                                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = FranchiseConstants.ENDPOINT_GET_TOP_PRODUCTS_BY_BRANCH,
                    method = RequestMethod.GET,
                    beanClass = FranchiseHandler.class,
                    beanMethod = "getTopStockProductsByBranch",
                    operation = @Operation(
                            summary = "Get Top Stock Products by Branch",
                            operationId = "getTopStockProductsByBranch",
                            parameters = {
                                    @io.swagger.v3.oas.annotations.Parameter(
                                            name = "franchiseId",
                                            description = "ID of the franchise to retrieve top stock products",
                                            required = true,
                                            in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Top stock products retrieved successfully",
                                            content = @Content(schema = @Schema(implementation = ProductUpdateStockRequestDto.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Invalid branch ID",
                                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Internal server error",
                                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = FranchiseConstants.ENDPOINT_UPDATE_FRANCHISE_NAME,
                    method = RequestMethod.PUT,
                    beanClass = FranchiseHandler.class,
                    beanMethod = "updateFranchiseName",
                    operation = @Operation(
                            summary = "Update Franchise Name",
                            operationId = "updateFranchiseName",
                            parameters = {
                                    @io.swagger.v3.oas.annotations.Parameter(
                                            name = "franchiseId",
                                            description = "ID of the franchise to update",
                                            required = true,
                                            in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH
                                    )
                            },
                            requestBody = @RequestBody(
                                    description = "DTO containing the new franchise name",
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = FranchiseUpdateNameDto.class))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Franchise name updated successfully",
                                            content = @Content(schema = @Schema(implementation = FranchiseResponseDto.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Franchise ID must be numeric or name must not be null or empty",
                                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Internal server error",
                                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
                                    )
                            }
                    )
            )
    })
    @Bean
    public RouterFunction<ServerResponse> routerFunction(FranchiseHandler handler) {
        return route(POST(FranchiseConstants.ENDPOINT_CREATE_FRANCHISE), handler::saveFranchise)
                .andRoute(GET(FranchiseConstants.ENDPOINT_GET_TOP_PRODUCTS_BY_BRANCH), handler::getTopStockProductsByBranch)
                .andRoute(PUT(FranchiseConstants.ENDPOINT_UPDATE_FRANCHISE_NAME), handler::updateFranchiseName);

    }
}
