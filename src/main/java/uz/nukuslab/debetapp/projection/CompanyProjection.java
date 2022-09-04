package uz.nukuslab.debetapp.projection;

import org.springframework.data.rest.core.config.Projection;
import uz.nukuslab.debetapp.entity.Company;

import java.util.UUID;

@Projection(types = Company.class)
public interface CompanyProjection {

    Long getId();

    String getName();

    boolean isActive();


}
