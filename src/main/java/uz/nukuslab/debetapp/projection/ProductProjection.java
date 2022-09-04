package uz.nukuslab.debetapp.projection;

import org.springframework.data.rest.core.config.Projection;
import uz.nukuslab.debetapp.entity.Product;

@Projection(types = Product.class)
public interface ProductProjection {

    Long getId();

    String getName();

    String getModel();

    boolean isActive();


}
