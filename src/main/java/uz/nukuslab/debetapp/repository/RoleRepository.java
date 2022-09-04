package uz.nukuslab.debetapp.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import uz.nukuslab.debetapp.annotation.CheckRole;
import uz.nukuslab.debetapp.entity.Role;
import uz.nukuslab.debetapp.projection.RoleProjection;

import java.util.List;
import java.util.Optional;

@CrossOrigin
//@CheckRole
//@PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN')")
@RepositoryRestResource(path = "role", excerptProjection = RoleProjection.class)
public interface RoleRepository extends JpaRepository<Role, Long> {

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN')")
    @Override
    List<Role> findAll();

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN')")
    @Override
    Optional<Role> findById(Long integer);

//    @CheckRole
//    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN')")
    @Override
    <S extends Role> S save(S entity);

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN')")
    @Override
    void deleteById(Long integer);

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN')")
    @Override
    void delete(Role entity);

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN')")
    @Override
    void deleteAllById(Iterable<? extends Long> integers);

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN')")
    @Override
    void deleteAll(Iterable<? extends Role> entities);

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN')")
    @Override
    void deleteAll();


}
