package uz.nukuslab.debetapp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseData {

    private int part;

    private double allPercentSumma;

    private double monthSumma;

    private double allSumma;

    private double monthPercentSumma;


}
