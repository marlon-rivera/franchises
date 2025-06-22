package co.com.nequi.franchising.api.dto.request;

public record ProductRequestDto(String name, Long branchId, Integer initialQuantity) {
}
