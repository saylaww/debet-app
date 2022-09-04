package uz.nukuslab.debetapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.nukuslab.debetapp.payload.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
//@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Contract extends AbsEntity {

//    @JoinColumn(nullable = false)
    @ManyToOne
    private Product product;

//    @JoinColumn(nullable = false)
    @ManyToOne
    private User worker;

    @Column(nullable = false)
    private double price;

//    @JoinColumn(nullable = false)
    @ManyToOne
    private Client client;

//    @JoinColumn(nullable = false)
//    @OneToMany
//    private List<Debet> debets;

    @Column(nullable = false)
    private double percent;

    @Column(nullable = false)
    private Integer part;

    private boolean enabled = false;

    public Contract(Product product, User worker, double price, Client client, double percent, Integer part) {
        this.product = product;
        this.worker = worker;
        this.price = price;
        this.client = client;
        this.percent = percent;
        this.part = part;
    }

}
