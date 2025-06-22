package co.com.nequi.franchising.api.dto.request;

public record ProductUpdateStockRequestDto(Long productId, Long branchId, Integer quantity) {
}
