package co.com.nequi.franchising.api.handler;

import co.com.nequi.franchising.api.dto.request.BranchRequestDto;
import co.com.nequi.franchising.api.dto.request.BranchUpdateNameDto;
import co.com.nequi.franchising.api.dto.response.BranchResponseDto;
import co.com.nequi.franchising.api.exception.ExceptionResponse;
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

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class BranchHandler {

    private final BranchUseCase branchUseCase;

    public Mono<ServerResponse> saveBranch(ServerRequest request) {
        return request.bodyToMono(BranchRequestDto.class)
                .flatMap(branchRequestDto -> {
                    if (branchRequestDto == null || branchRequestDto.name() == null || branchRequestDto.name().isBlank()) {
                        ExceptionResponse errorResponse = new ExceptionResponse(
                                LocalDateTime.now().toString(), HttpStatus.BAD_REQUEST.value(),
                                BranchConstants.ERROR_BRANCH_NAME_NOT_NULL_OR_EMPTY, BranchConstants.ENDPOINT_CREATE_BRANCH
                        );
                        return ServerResponse.badRequest()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(errorResponse);
                    }
                    if (branchRequestDto.franchiseId() == null) {
                        ExceptionResponse errorResponse = new ExceptionResponse(
                                LocalDateTime.now().toString(), HttpStatus.BAD_REQUEST.value(),
                                BranchConstants.ERROR_BRANCH_FRANCHISE_ID_NOT_VALID, BranchConstants.ENDPOINT_CREATE_BRANCH
                        );
                        return ServerResponse.badRequest()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(errorResponse);
                    }
                    Branch branch = Branch.builder()
                            .name(branchRequestDto.name())
                            .franchiseId(branchRequestDto.franchiseId())
                            .build();

                    return branchUseCase.saveBranch(branch)
                            .flatMap(savedBranch -> {
                                BranchResponseDto responseDto = new BranchResponseDto(
                                        savedBranch.getId(),
                                        savedBranch.getName(),
                                        savedBranch.getFranchiseId()
                                );
                                return ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(responseDto);
                            });
                });
    }

    public Mono<ServerResponse> updateBranchName(ServerRequest request) {
        Long branchId;
        try{
            branchId = Long.valueOf(request.pathVariable(BranchConstants.PATH_VARIABLE_BRANCH_ID));
        }catch (NumberFormatException e){
            ExceptionResponse errorResponse = new ExceptionResponse(
                    LocalDateTime.now().toString(), HttpStatus.BAD_REQUEST.value(),
                    BranchConstants.ERROR_BRANCH_ID_MUST_BE_NUMERIC, BranchConstants.ENDPOINT_UPDATE_BRANCH_NAME
            );
            return ServerResponse.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(errorResponse);
        }
        return request.bodyToMono(BranchUpdateNameDto.class)
                .flatMap(branchUpdateNameDto -> {
                    if (branchUpdateNameDto == null || branchUpdateNameDto.name() == null || branchUpdateNameDto.name().isBlank()) {
                        ExceptionResponse errorResponse = new ExceptionResponse(
                                LocalDateTime.now().toString(), HttpStatus.BAD_REQUEST.value(),
                                BranchConstants.ERROR_BRANCH_NAME_NOT_NULL_OR_EMPTY, BranchConstants.ENDPOINT_UPDATE_BRANCH_NAME
                        );
                        return ServerResponse.badRequest()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(errorResponse);
                    }
                    return branchUseCase.updateBranchName(branchId, branchUpdateNameDto.name())
                            .flatMap(updatedBranch -> {
                                BranchResponseDto responseDto = new BranchResponseDto(
                                        updatedBranch.getId(),
                                        updatedBranch.getName(),
                                        updatedBranch.getFranchiseId()
                                );
                                return ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(responseDto);
                            });
                });
    }


}
