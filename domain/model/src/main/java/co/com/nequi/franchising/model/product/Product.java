package co.com.nequi.franchising.model.product;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Product {

    private Long id;
    private String name;

}
