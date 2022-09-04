package uz.nukuslab.debetapp.annotation;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import uz.nukuslab.debetapp.entity.User;

@Component
@Aspect
public class CheckRoleExecuter {


    @Before(value = "@annotation(checkRole)")
    public void checkUserPermissionMyMethod(CheckRole checkRole){
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(user.getRole().getRoleName().name());
//        System.out.println(checkRole.value());

//        boolean bool = false;

        if (user.isAccountNonExpired() &&
                user.isAccountNonLocked() &&
                user.isCredentialsNonExpired() &&
                user.isEnabled()){
//            if (checkRole.value().equals("USER")){
//                for (GrantedAuthority authority : user.getAuthorities()) {
//                    if (authority.getAuthority().equals("USER") ||
//                        authority.getAuthority().equals("ADMIN") ||
//                        authority.getAuthority().equals("SUPER_ADMIN")){
//                        bool = true;
//                    }
//                }
//            }
//
//            for (GrantedAuthority authority : user.getAuthorities()) {
//                    if (authority.getAuthority().equals(checkRole.value())){
//                        bool = true;
//                    }
//
//            }


//            for (GrantedAuthority authority : user.getAuthorities()) {
//                if (authority.getAuthority().equals(checkRole.value())){
//                    bool = true;
//                }
//            }
//
//            if (!bool) {
//                throw new Forbidden();
//            }

        }else {
            throw new UnAuthoized();
        }



    }


}
