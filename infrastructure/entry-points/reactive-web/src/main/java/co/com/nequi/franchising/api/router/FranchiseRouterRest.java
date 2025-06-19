package co.com.nequi.franchising.api.router;

import co.com.nequi.franchising.api.dto.request.FranchiseRequestDto;
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

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
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
            )
    })
    @Bean
    public RouterFunction<ServerResponse> routerFunction(FranchiseHandler handler) {
        return route(POST(FranchiseConstants.ENDPOINT_CREATE_FRANCHISE), handler::saveFranchise);
    }
}
