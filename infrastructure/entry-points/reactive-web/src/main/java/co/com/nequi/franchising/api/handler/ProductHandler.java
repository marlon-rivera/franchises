package co.com.nequi.franchising.api.handler;

import co.com.nequi.franchising.api.dto.request.ProductRequestDto;
import co.com.nequi.franchising.api.dto.request.ProductUpdateNameDto;
import co.com.nequi.franchising.api.dto.request.ProductUpdateStockRequestDto;
import co.com.nequi.franchising.api.util.RequestUtils;
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

@Component
@RequiredArgsConstructor
public class ProductHandler {

    private final ProductUseCase productUseCase;

    public Mono<ServerResponse> saveProduct(ServerRequest request) {
        return RequestUtils.requireBody(request, ProductRequestDto.class)
                .flatMap(dto -> {
                    Product product = Product.builder()
                            .name(dto.name()).build();
                    return productUseCase.saveProduct(product, dto.initialQuantity(), dto.branchId())
                            .flatMap(savedProduct -> ServerResponse.status(HttpStatus.CREATED)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(savedProduct));
                });
    }

    public Mono<ServerResponse> deleteProductFromBranch(ServerRequest request) {
        return Mono.zip(
                RequestUtils.parsePathVariableToLong(request, ProductConstants.PATH_VARIABLE_PRODUCT_ID),
                RequestUtils.parsePathVariableToLong(request, ProductConstants.PATH_VARIABLE_BRANCH_ID)
        ).flatMap(tuple -> productUseCase.deleteProductFromBranch(tuple.getT1(), tuple.getT2())
                .then(ServerResponse.noContent().build()));
    }

    public Mono<ServerResponse> updateStockProduct(ServerRequest request) {
        return RequestUtils.requireBody(request, ProductUpdateStockRequestDto.class)
                .flatMap(dto -> productUseCase.updateStock(dto.productId(), dto.branchId(), dto.quantity())
                        .flatMap(updatedBranchProduct -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(updatedBranchProduct)));
    }

    public Mono<ServerResponse> updateProductName(ServerRequest request) {
        return RequestUtils.parsePathVariableToLong(request, ProductConstants.PATH_VARIABLE_PRODUCT_ID)
                .flatMap(productId -> RequestUtils.requireBody(request, ProductUpdateNameDto.class)
                        .flatMap(dto -> productUseCase.updateProductName(productId, dto.name())
                                .flatMap(updatedProduct -> ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(updatedProduct))));
    }
}
