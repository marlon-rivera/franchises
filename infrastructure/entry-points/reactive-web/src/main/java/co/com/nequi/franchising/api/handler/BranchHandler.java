package co.com.nequi.franchising.api.handler;

import co.com.nequi.franchising.api.dto.request.BranchRequestDto;
import co.com.nequi.franchising.api.dto.request.BranchUpdateNameDto;
import co.com.nequi.franchising.api.dto.response.BranchResponseDto;
import co.com.nequi.franchising.api.util.RequestUtils;
import co.com.nequi.franchising.model.branch.Branch;
import co.com.nequi.franchising.usecase.branch.BranchUseCase;
import co.com.nequi.franchising.utils.constants.BranchConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BranchHandler {

    private final BranchUseCase branchUseCase;

    public Mono<ServerResponse> saveBranch(ServerRequest request) {
        return RequestUtils.requireBody(request, BranchRequestDto.class)
                .flatMap(dto -> {
                    Branch branch = Branch.builder()
                            .name(dto.name())
                            .franchiseId(dto.franchiseId())
                            .build();
                    return branchUseCase.saveBranch(branch)
                            .flatMap(savedBranch -> {
                                BranchResponseDto responseDto = new BranchResponseDto(
                                        savedBranch.getId(),
                                        savedBranch.getName(),
                                        savedBranch.getFranchiseId()
                                );
                                return ServerResponse.status(HttpStatus.CREATED)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(responseDto);
                            });
                });
    }

    public Mono<ServerResponse> updateBranchName(ServerRequest request) {
        return RequestUtils.parsePathVariableToLong(request, BranchConstants.PATH_VARIABLE_BRANCH_ID)
                .flatMap(branchId -> RequestUtils.requireBody(request, BranchUpdateNameDto.class)
                        .flatMap(dto -> branchUseCase.updateBranchName(branchId, dto.name())
                                .flatMap(updatedBranch -> {
                                    BranchResponseDto responseDto = new BranchResponseDto(
                                            updatedBranch.getId(),
                                            updatedBranch.getName(),
                                            updatedBranch.getFranchiseId()
                                    );
                                    return ServerResponse.ok()
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .bodyValue(responseDto);
                                })));
    }
}
