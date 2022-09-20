package uz.nukuslab.debetapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.nukuslab.debetapp.payload.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Rule extends AbsEntity {

    private double price = 0;
    private double priceBusiness = 0;

    private String sendHour;
    private String sendMinute;

    public Rule(double price, double priceBusiness) {
        this.price = price;
        this.priceBusiness = priceBusiness;
    }
}
