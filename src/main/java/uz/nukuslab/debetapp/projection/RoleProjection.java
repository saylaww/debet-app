package uz.nukuslab.debetapp.projection;

import org.springframework.data.rest.core.config.Projection;
import uz.nukuslab.debetapp.entity.Role;

@Projection(types = Role.class)
public interface RoleProjection {

    Long getId();

    String getRoleName();

}
