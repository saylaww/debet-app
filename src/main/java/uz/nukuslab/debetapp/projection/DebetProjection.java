package uz.nukuslab.debetapp.projection;

import org.springframework.data.rest.core.config.Projection;
import uz.nukuslab.debetapp.entity.Contract;
import uz.nukuslab.debetapp.entity.Debet;

@Projection(types = Debet.class)
public interface DebetProjection {

    Long getId();

    String getMonthName();

    String getModel();

    double getSumma();

    Contract getContract();

    boolean isActive();


}
