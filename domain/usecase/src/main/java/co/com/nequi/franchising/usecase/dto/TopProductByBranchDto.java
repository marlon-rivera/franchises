package co.com.nequi.franchising.usecase.dto;

import co.com.nequi.franchising.model.branch.Branch;
import co.com.nequi.franchising.model.product.Product;

public record TopProductByBranchDto(Branch branch, Product product, Integer stock) {
}
