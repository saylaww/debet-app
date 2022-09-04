package uz.nukuslab.debetapp.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import uz.nukuslab.debetapp.annotation.CheckRole;
import uz.nukuslab.debetapp.entity.Product;
import uz.nukuslab.debetapp.projection.ProductProjection;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RepositoryRestResource(path = "product", excerptProjection = ProductProjection.class)
public interface ProductRepository extends JpaRepository<Product, Long> {

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN', 'USER')")
    @Override
    List<Product> findAll();

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN', 'USER')")
    @Override
    Optional<Product> findById(Long integer);

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN', 'USER')")
    @Override
    <S extends Product> S save(S entity);

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN', 'USER')")
    @Override
    void deleteById(Long integer);

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN', 'USER')")
    @Override
    void delete(Product entity);

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN', 'USER')")
    @Override
    void deleteAllById(Iterable<? extends Long> integers);

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN', 'USER')")
    @Override
    void deleteAll(Iterable<? extends Product> entities);

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN', 'USER')")
    @Override
    void deleteAll();
}
