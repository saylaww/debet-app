package uz.nukuslab.debetapp.payload;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class Data {

    private double summa;

    private int part;

    private double percent;


}
