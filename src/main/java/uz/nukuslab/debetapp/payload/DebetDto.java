package uz.nukuslab.debetapp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.nukuslab.debetapp.entity.Contract;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DebetDto {

    private String monthName;

    private double summa;

    private Contract contract;

    private boolean paid;


}
