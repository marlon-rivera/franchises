package co.com.nequi.franchising.r2dbc.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("product_branch")
public class ProductBranchData {

    @Id
    private Long id;
    private Long productId;
    private Long branchId;
    private Integer stock;

}
