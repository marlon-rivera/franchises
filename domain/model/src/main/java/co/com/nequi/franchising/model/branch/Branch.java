package co.com.nequi.franchising.model.branch;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Branch {

    private Long id;
    private String name;
    private Long franchiseId;

}
