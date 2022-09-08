package uz.nukuslab.debetapp.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import uz.nukuslab.debetapp.annotation.CheckRole;
import uz.nukuslab.debetapp.entity.User;
import uz.nukuslab.debetapp.entity.enums.RoleName;
import uz.nukuslab.debetapp.projection.UserProjection;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RepositoryRestResource(path = "user", excerptProjection = UserProjection.class)
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);


    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @Override
    List<User> findAll();

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @Override
    Optional<User> findById(Long integer);

//    @CheckRole
//    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @Override
    <S extends User> S save(S entity);

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @Override
    void deleteById(Long integer);

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @Override
    void delete(User entity);

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN', 'USER')")
    @Override
    void deleteAllById(Iterable<? extends Long> integers);

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN', 'USER')")
    @Override
    void deleteAll(Iterable<? extends User> entities);

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN', 'USER')")
    @Override
    void deleteAll();




    Optional<User> findByUsername(String username);

    List<User> findByCompanyId(Long company_id);

    List<User> findByCompany_IdAndRole_RoleName(Long company_id, RoleName role_roleName);

//    User findByCompany_IdAndRole_RoleNameAndCompany_Active(Integer company_id, RoleName role_roleName, boolean company_active);

    User findByIdAndCompany_IdAndRole_RoleNameAndCompany_Active(Long id, Long company_id, RoleName role_roleName, boolean company_active);

    Optional<User> findByIdAndRole_RoleName(Long id, RoleName role_roleName);

//    List<User> findByCreatedAtBetween(Timestamp createdAt, Timestamp createdAt2);

    List<User> findByRole_RoleNameOrRole_RoleName(RoleName role_roleName, RoleName role_roleName2);

    Optional<User> findByIdAndCompanyActive(Long id, boolean company_active);

}
