package co.com.nequi.franchising.api.handler;

import co.com.nequi.franchising.api.dto.request.FranchiseRequestDto;
import co.com.nequi.franchising.api.dto.response.FranchiseResponseDto;
import co.com.nequi.franchising.api.exception.ExceptionResponse;
import co.com.nequi.franchising.model.franchise.Franchise;
import co.com.nequi.franchising.usecase.franchise.FranchiseUseCase;
import co.com.nequi.franchising.utils.constants.FranchiseConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class FranchiseHandler {

    private final FranchiseUseCase franchiseUseCase;

    public Mono<ServerResponse> saveFranchise(ServerRequest request) {
        return request.bodyToMono(FranchiseRequestDto.class)
                .flatMap(franchiseRequestDto -> {
                    if(franchiseRequestDto == null || franchiseRequestDto.name() == null || franchiseRequestDto.name().isBlank()) {
                        ExceptionResponse errorResponse = new ExceptionResponse(
                                LocalDateTime.now().toString(), HttpStatus.BAD_REQUEST.value(),
                                FranchiseConstants.ERROR_FRANCHISE_NAME_NOT_NULL_OR_EMPTY, FranchiseConstants.ENDPOINT_CREATE_FRANCHISE);
                        return ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON).bodyValue(errorResponse);
                    }
                    Franchise franchise = Franchise.builder().name(franchiseRequestDto.name())
                            .build();
                    return franchiseUseCase.saveFranchise(franchise)
                                .flatMap(savedFranchise -> {
                                    FranchiseResponseDto responseDto = new FranchiseResponseDto(savedFranchise.getId(), savedFranchise.getName());
                                    return ServerResponse.ok()
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .bodyValue(responseDto);
                                });
                });
    }
}
