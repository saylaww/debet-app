package uz.nukuslab.debetapp.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import uz.nukuslab.debetapp.annotation.CheckRole;
import uz.nukuslab.debetapp.entity.Company;
import uz.nukuslab.debetapp.projection.CompanyProjection;

import java.util.Optional;
import java.util.UUID;

@CrossOrigin
@RepositoryRestResource(path = "company", excerptProjection = CompanyProjection.class)
public interface CompanyRepository extends CrudRepository<Company, Long> {

    @CheckRole
//    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN')")
    @Override
    <S extends Company> S save(S entity);


//    @CheckRole
//    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN')")
//    @Override
//    Optional<Company> findById(UUID uuid);

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN')")
    @Override
    Optional<Company> findById(Long integer);

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN')")
    @Override
    Iterable<Company> findAll();

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN')")
    @Override
    void deleteById(Long integer);
}
