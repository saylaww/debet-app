package uz.nukuslab.debetapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import uz.nukuslab.debetapp.payload.AbsEntity;
import uz.nukuslab.debetapp.repository.ContractRepository;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Optional;

//@JsonIgnoreProperties({ "contractRepository" })
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Debet extends AbsEntity {

//    @Autowired
//    ContractRepository contractRepository;

    @Column(nullable = false)
    private String monthName;

//    @Column(nullable = false)
//    private String model;

    @Column(nullable = false)
    private double summa;

    @ManyToOne
    private Contract contract;

    private boolean paid = false;

    private Timestamp payDate;

    public Debet(String monthName, double summa, Contract contract) {
        this.monthName = monthName;
        this.summa = summa;
        this.contract = contract;
    }

    public Debet(String monthName, double summa, Contract contract, Timestamp payDate) {
        this.monthName = monthName;
        this.summa = summa;
        this.contract = contract;
        this.payDate = payDate;
    }
}
