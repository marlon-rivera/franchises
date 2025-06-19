package co.com.nequi.franchising.model.franchise;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Franchise {

    private Long id;
    private String name;

}
