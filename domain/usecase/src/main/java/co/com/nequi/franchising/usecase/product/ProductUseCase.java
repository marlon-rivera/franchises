package co.com.nequi.franchising.usecase.product;

import co.com.nequi.franchising.model.branch.gateways.BranchRepository;
import co.com.nequi.franchising.model.branchproduct.BranchProduct;
import co.com.nequi.franchising.model.branchproduct.gateways.BranchProductRepository;
import co.com.nequi.franchising.model.exceptions.BranchNotExistException;
import co.com.nequi.franchising.model.exceptions.InvalidDataException;
import co.com.nequi.franchising.model.exceptions.ProductCreationException;
import co.com.nequi.franchising.model.exceptions.ProductNotExistException;
import co.com.nequi.franchising.model.product.Product;
import co.com.nequi.franchising.model.product.gateways.ProductRepository;
import co.com.nequi.franchising.usecase.dto.ProductDto;
import co.com.nequi.franchising.usecase.utils.BranchMessagesConstants;
import co.com.nequi.franchising.usecase.utils.ProductMessagesConstants;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ProductUseCase {

    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;
    private final BranchProductRepository branchProductRepository;

    public Mono<ProductDto> saveProduct(Product product, Integer quantity, Long branchId) {
        return validateProduct(product, quantity, branchId)
                .then(Mono.defer(() -> branchRepository.findById(branchId)))
                .switchIfEmpty(Mono.error(new BranchNotExistException(BranchMessagesConstants.ERROR_BRANCH_NOT_EXIST + branchId)))
                .flatMap(branch -> productRepository.save(product)
                        .flatMap(savedProduct ->
                                branchProductRepository.save(BranchProduct.builder().productId(savedProduct.getId()).branchId(branchId).stock(quantity).build())
                                        .thenReturn(new ProductDto(savedProduct.getId(), savedProduct.getName(), quantity, branchId))
                        ));
    }

    public Mono<Void> deleteProductFromBranch(Long productId, Long branchId) {
        return Mono.defer(() -> {
            if (productId == null || branchId == null) {
                return Mono.error(new ProductCreationException(ProductMessagesConstants.ERROR_PRODUCT_ID_OR_BRANCH_ID_NOT_VALID));
            }
            return branchProductRepository.findByProductIdAndBranchId(productId, branchId)
                    .switchIfEmpty(Mono.error(new InvalidDataException(ProductMessagesConstants.ERROR_COMBINATION_PRODUCT_BRANCH_NOT_EXIST)))
                    .flatMap(branchProductRepository::delete);
        });
    }

    public Mono<BranchProduct> updateStock(Long productId, Long branchId, Integer quantity) {
        return validateUpdateStock(productId, branchId, quantity)
                .then(Mono.defer(() -> branchProductRepository.findByProductIdAndBranchId(productId, branchId)))
                .switchIfEmpty(Mono.error(new InvalidDataException(ProductMessagesConstants.ERROR_COMBINATION_PRODUCT_BRANCH_NOT_EXIST)))
                .flatMap(branchProduct -> branchProductRepository.save(
                        branchProduct.toBuilder().stock(quantity).build()
                ));
    }

    public Mono<Product> updateProductName(Long productId, String newName) {
        return Mono.defer(() -> {
            if (newName == null || newName.isBlank()) {
                return Mono.error(new InvalidDataException(ProductMessagesConstants.ERROR_PRODUCT_NAME_NOT_NULL_OR_EMPTY));
            }
            return productRepository.findById(productId)
                    .switchIfEmpty(Mono.error(new ProductNotExistException(ProductMessagesConstants.ERROR_PRODUCT_NOT_EXIST + productId)))
                    .flatMap(product -> productRepository.save(
                            product.toBuilder().name(newName).build()
                    ));
        });
    }

    private Mono<Void> validateUpdateStock(Long productId, Long branchId, Integer quantity) {
        if (productId == null || branchId == null) {
            return Mono.error(new ProductCreationException(ProductMessagesConstants.ERROR_PRODUCT_ID_OR_BRANCH_ID_NOT_VALID));
        }
        if (quantity == null) {
            return Mono.error(new ProductCreationException(ProductMessagesConstants.ERROR_PRODUCT_QUANTITY_NOT_NULL));
        }
        if (quantity < 0) {
            return Mono.error(new ProductCreationException(ProductMessagesConstants.ERROR_PRODUCT_QUANTITY_NOT_VALID));
        }
        return Mono.empty();
    }

    private Mono<Void> validateProduct(Product product, Integer quantity, Long branchId) {
        if (product == null || product.getName() == null || product.getName().isBlank()) {
            return Mono.error(new ProductCreationException(ProductMessagesConstants.ERROR_PRODUCT_NAME_NOT_NULL_OR_EMPTY));
        }
        if (quantity == null) {
            return Mono.error(new ProductCreationException(ProductMessagesConstants.ERROR_PRODUCT_QUANTITY_NOT_NULL));
        }
        if (quantity < 0) {
            return Mono.error(new ProductCreationException(ProductMessagesConstants.ERROR_PRODUCT_QUANTITY_NOT_VALID));
        }
        if (branchId == null) {
            return Mono.error(new ProductCreationException(ProductMessagesConstants.ERROR_PRODUCT_BRANCH_ID_NOT_VALID));
        }
        return Mono.empty();
    }
}
