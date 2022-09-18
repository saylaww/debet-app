package uz.nukuslab.debetapp.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import uz.nukuslab.debetapp.entity.Client;
import uz.nukuslab.debetapp.projection.ClientProjection;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RepositoryRestResource(path = "client", excerptProjection = ClientProjection.class)
public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findByCompany_Id(Long company_id);

    boolean existsByPhone(String phone);
    boolean existsByPhoneAndCreatedBy(String phone, Long createdBy);

//    List<Client> findByCompany_




}
