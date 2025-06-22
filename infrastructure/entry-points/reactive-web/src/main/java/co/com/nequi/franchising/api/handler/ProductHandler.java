package co.com.nequi.franchising.api.handler;

import co.com.nequi.franchising.api.dto.request.ProductRequestDto;
import co.com.nequi.franchising.api.dto.request.ProductUpdateStockRequestDto;
import co.com.nequi.franchising.api.exception.ExceptionResponse;
import co.com.nequi.franchising.model.product.Product;
import co.com.nequi.franchising.usecase.product.ProductUseCase;
import co.com.nequi.franchising.utils.constants.ProductConstants;
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
public class ProductHandler {

    private final ProductUseCase productUseCase;

    public Mono<ServerResponse> saveProduct(ServerRequest request){
        return request.bodyToMono(ProductRequestDto.class)
                .flatMap(productRequestDto -> {
                    ExceptionResponse errorResponse = validateProductRequest(productRequestDto);
                    if (errorResponse != null) {
                        return ServerResponse.badRequest()
                                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                                .bodyValue(errorResponse);
                    }
                    Product product = Product.builder()
                            .name(productRequestDto.name()).build();

                    return productUseCase.saveProduct(product, productRequestDto.initialQuantity(), productRequestDto.branchId())
                            .flatMap(savedProduct -> ServerResponse.status(HttpStatus.CREATED)
                                .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(savedProduct));
                });
    }

    private ExceptionResponse validateProductRequest(ProductRequestDto productRequestDto) {
        String message = "";

        if (productRequestDto == null || productRequestDto.name() == null || productRequestDto.name().isBlank()) {
            message = ProductConstants.ERROR_PRODUCT_NAME_NOT_NULL_OR_EMPTY;
        } else if (productRequestDto.branchId() == null) {
            message = ProductConstants.ERROR_PRODUCT_BRANCH_ID_NOT_VALID;
        } else if (productRequestDto.initialQuantity() == null) {
            message = ProductConstants.ERROR_PRODUCT_QUANTITY_NOT_NULL;
        }else if (productRequestDto.initialQuantity() < 0) {
            message = ProductConstants.ERROR_PRODUCT_QUANTITY_NOT_VALID;
        }
        if (!message.isEmpty()) {
            return new ExceptionResponse(
                    LocalDateTime.now().toString(),
                    HttpStatus.BAD_REQUEST.value(),
                    message,
                    ProductConstants.ENDPOINT_CREATE_PRODUCT
            );
        }
        return null;
    }

    public Mono<ServerResponse> deleteProductFromBranch(ServerRequest request) {
        return Mono.defer(() -> {
            try {
                Long productId = Long.valueOf(request.pathVariable(ProductConstants.PATH_VARIABLE_PRODUCT_ID));
                Long branchId = Long.valueOf(request.pathVariable(ProductConstants.PATH_VARIABLE_BRANCH_ID));

                return productUseCase.deleteProductFromBranch(productId, branchId)
                        .then(ServerResponse.noContent().build());

            } catch (NumberFormatException e) {
                ExceptionResponse errorResponse = new ExceptionResponse(
                        LocalDateTime.now().toString(),
                        HttpStatus.BAD_REQUEST.value(),
                        ProductConstants.ERROR_PRODUCT_ID_OR_BRANCH_ID_NOT_VALID,
                        ProductConstants.ENDPOINT_DELETE_PRODUCT_FROM_BRANCH
                );
                return ServerResponse.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(errorResponse);
            }
        });
    }

    public Mono<ServerResponse> updateStockProduct(ServerRequest request) {
        return request.bodyToMono(ProductUpdateStockRequestDto.class)
                .flatMap(productUpdateStockRequestDto -> {
                    ExceptionResponse errorResponse = validateUpdateStockRequest(productUpdateStockRequestDto);
                    if (errorResponse != null) {
                        return ServerResponse.badRequest()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(errorResponse);
                    }
                    return productUseCase.updateStock(
                            productUpdateStockRequestDto.productId(),
                            productUpdateStockRequestDto.branchId(),
                            productUpdateStockRequestDto.quantity()
                    )
                            .flatMap(updatedBranchProduct -> ServerResponse.ok()
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(updatedBranchProduct));
                });
    }

    private ExceptionResponse validateUpdateStockRequest(ProductUpdateStockRequestDto productUpdateStockRequestDto) {
        String message = "";

        if (productUpdateStockRequestDto == null || productUpdateStockRequestDto.productId() == null) {
            message = ProductConstants.ERROR_PRODUCT_ID_OR_BRANCH_ID_NOT_VALID;
        } else if (productUpdateStockRequestDto.branchId() == null) {
            message = ProductConstants.ERROR_PRODUCT_BRANCH_ID_NOT_VALID;
        } else if (productUpdateStockRequestDto.quantity() == null) {
            message = ProductConstants.ERROR_PRODUCT_QUANTITY_NOT_NULL;
        } else if (productUpdateStockRequestDto.quantity() < 0) {
            message = ProductConstants.ERROR_PRODUCT_QUANTITY_NOT_VALID;
        }

        if (!message.isEmpty()) {
            return new ExceptionResponse(
                    LocalDateTime.now().toString(),
                    HttpStatus.BAD_REQUEST.value(),
                    message,
                    ProductConstants.ENDPOINT_UPDATE_STOCK
            );
        }
        return null;
    }

}
