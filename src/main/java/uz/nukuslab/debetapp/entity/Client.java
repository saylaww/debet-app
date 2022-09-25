package uz.nukuslab.debetapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.nukuslab.debetapp.payload.AbsEntity;

import javax.persistence.*;
//import javax.validation.Constraint;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
//@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"createdBy", "phone"})
})
public class Client extends AbsEntity {

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

//    @Column(nullable = false, unique = true)
    @Column(nullable = false, unique = true)
    private String phone;

    @ManyToOne
    private Company company;


}
