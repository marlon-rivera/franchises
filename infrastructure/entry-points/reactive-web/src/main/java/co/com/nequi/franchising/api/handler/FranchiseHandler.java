package co.com.nequi.franchising.api.handler;

import co.com.nequi.franchising.api.dto.request.FranchiseRequestDto;
import co.com.nequi.franchising.api.dto.request.FranchiseUpdateNameDto;
import co.com.nequi.franchising.api.dto.response.FranchiseResponseDto;
import co.com.nequi.franchising.api.util.RequestUtils;
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

@Component
@RequiredArgsConstructor
public class FranchiseHandler {

    private final FranchiseUseCase franchiseUseCase;

    public Mono<ServerResponse> saveFranchise(ServerRequest request) {
        return RequestUtils.requireBody(request, FranchiseRequestDto.class)
                .flatMap(dto -> {
                    Franchise franchise = Franchise.builder().name(dto.name()).build();
                    return franchiseUseCase.saveFranchise(franchise)
                            .flatMap(savedFranchise -> {
                                FranchiseResponseDto responseDto = new FranchiseResponseDto(savedFranchise.getId(), savedFranchise.getName());
                                return ServerResponse.status(HttpStatus.CREATED)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(responseDto);
                            });
                });
    }

    public Mono<ServerResponse> getTopStockProductsByBranch(ServerRequest request) {
        return RequestUtils.parsePathVariableToLong(request, FranchiseConstants.PATH_VARIABLE_FRANCHISE_ID)
                .flatMapMany(franchiseUseCase::getTopStockProductsByBranch)
                .collectList()
                .flatMap(list -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(list));
    }

    public Mono<ServerResponse> updateFranchiseName(ServerRequest request) {
        return RequestUtils.parsePathVariableToLong(request, FranchiseConstants.PATH_VARIABLE_FRANCHISE_ID)
                .flatMap(franchiseId -> RequestUtils.requireBody(request, FranchiseUpdateNameDto.class)
                        .flatMap(dto -> franchiseUseCase.updateName(franchiseId, dto.name())
                                .flatMap(updated -> ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(new FranchiseResponseDto(updated.getId(), updated.getName())))));
    }
}
