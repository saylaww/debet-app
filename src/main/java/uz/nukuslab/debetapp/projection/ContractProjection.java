package uz.nukuslab.debetapp.projection;

import org.springframework.data.rest.core.config.Projection;
import uz.nukuslab.debetapp.entity.*;

import java.util.List;
import java.util.UUID;

@Projection(types = Contract.class)
public interface ContractProjection {

    Long getId();

    Product getProduct();

    User getWorker();

    double getPrice();

    Client getClient();

//    List<Debet> getDebets();

    double getPercent();

    Integer getPart();

}
