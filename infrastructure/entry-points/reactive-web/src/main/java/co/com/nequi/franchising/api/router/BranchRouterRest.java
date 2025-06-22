package co.com.nequi.franchising.api.router;

import co.com.nequi.franchising.api.dto.request.BranchRequestDto;
import co.com.nequi.franchising.api.dto.response.BranchResponseDto;
import co.com.nequi.franchising.api.exception.ExceptionResponse;
import co.com.nequi.franchising.api.handler.BranchHandler;
import co.com.nequi.franchising.utils.constants.BranchConstants;
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
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class BranchRouterRest {

    @RouterOperations({
            @RouterOperation(
                    path = BranchConstants.ENDPOINT_CREATE_BRANCH,
                    method = RequestMethod.POST,
                    beanClass = BranchHandler.class,
                    beanMethod = "saveBranch",
                    operation = @Operation(
                            summary = "Create Branch",
                            operationId = "createBranch",
                            requestBody = @RequestBody(
                                    description = "Entry DTO to create a branch",
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = BranchRequestDto.class))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Branch created successfully",
                                            content = @Content(schema = @Schema(implementation = BranchResponseDto.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Branch data is invalid or missing",
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
    public RouterFunction<ServerResponse> branchRoutes(BranchHandler handler) {
        return route(POST(BranchConstants.ENDPOINT_CREATE_BRANCH), handler::saveBranch)
                .andRoute(PUT(BranchConstants.ENDPOINT_UPDATE_BRANCH_NAME), handler::updateBranchName);
    }

}
