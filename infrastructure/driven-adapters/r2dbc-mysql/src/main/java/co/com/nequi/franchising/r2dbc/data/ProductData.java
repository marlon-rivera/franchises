package co.com.nequi.franchising.r2dbc.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("product")
public class ProductData {

    @Id
    private Long id;
    private String name;

}
