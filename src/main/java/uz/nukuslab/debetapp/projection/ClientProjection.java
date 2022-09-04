package uz.nukuslab.debetapp.projection;

import org.springframework.data.rest.core.config.Projection;
import uz.nukuslab.debetapp.entity.Client;

import java.util.UUID;

@Projection(types = Client.class)
public interface ClientProjection {

    Long getId();

    String getFirstName();

    String getLastName();

    String getPhone();


}
