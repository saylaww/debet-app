package uz.nukuslab.debetapp.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import uz.nukuslab.debetapp.entity.Client;
import uz.nukuslab.debetapp.projection.ClientProjection;

import java.util.UUID;

@CrossOrigin
@RepositoryRestResource(path = "client", excerptProjection = ClientProjection.class)
public interface ClientRepository extends JpaRepository<Client, Long> {



}
