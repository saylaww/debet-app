package uz.nukuslab.debetapp.annotation;

import org.springframework.security.core.GrantedAuthority;
import uz.nukuslab.debetapp.entity.Role;

import java.lang.annotation.*;
import java.util.List;
import java.util.Set;

@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckRole {

//    String value();

}
