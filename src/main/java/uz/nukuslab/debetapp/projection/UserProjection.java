package uz.nukuslab.debetapp.projection;

import org.springframework.data.rest.core.config.Projection;
import uz.nukuslab.debetapp.entity.Company;
import uz.nukuslab.debetapp.entity.User;

@Projection(types = User.class)
public interface UserProjection {

    Long getId();

    String getFirstName();

    String getLastName();

    String getPhone();

    String getUsername();

    String getPassword();

    Company getCompany();

}
