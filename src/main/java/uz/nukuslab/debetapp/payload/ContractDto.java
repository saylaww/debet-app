package uz.nukuslab.debetapp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractDto {

    private String productName;

    private Long workerId;

    private double price;

    private Long clientId;

    private double percent;

    private Integer part;


}
