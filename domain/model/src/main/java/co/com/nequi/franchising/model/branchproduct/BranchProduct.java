package co.com.nequi.franchising.model.branchproduct;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class BranchProduct {

    private Long id;
    private Long branchId;
    private Long productId;
    private Integer stock;

}
