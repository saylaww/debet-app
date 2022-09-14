package uz.nukuslab.debetapp.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import uz.nukuslab.debetapp.annotation.CheckRole;
import uz.nukuslab.debetapp.entity.Company;
import uz.nukuslab.debetapp.entity.Contract;
import uz.nukuslab.debetapp.projection.ContractProjection;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RepositoryRestResource(path = "contract", excerptProjection = ContractProjection.class)
public interface ContractRepository extends JpaRepository<Contract, Long> {

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    @Override
    <S extends Contract> List<S> saveAll(Iterable<S> entities);

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    @Override
    <S extends Contract> S save(S entity);

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    @Override
    Optional<Contract> findById(Long integer);

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    @Override
    void deleteById(Long integer);


    //    @CheckRole
//    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    List<Contract> findByClient_Phone(String client_phone);


//    List<Contract> findByClient_PhoneContaining(String client_phone);
//    List<Contract> findByProductNameContaining(String productName);
//    List<Contract> findByProductNameContainingIgnoreCase(String productName);
    List<Contract> findByClient_PhoneContaining(String client_phone);
    List<Contract> findByClient_PhoneContainingAndWorker_Company_IdAndClient_Company_Id(String client_phone, Long worker_company_id, Long client_company_id);

    List<Contract>findByEnabledAndClient_Company_IdAndWorker_Company_Id(boolean enabled, Long client_company_id, Long worker_company_id);

//    List<Debet> findByContract_Worker_CompanyAndCreatedAtBetweenAndContract_Worker_CompanyActive(Company contract_worker_company, Timestamp createdAt, Timestamp createdAt2, boolean contract_worker_company_active);

//    @CheckRole
//    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    List<Contract> findByWorker_CompanyAndCreatedAtBetweenAndWorker_CompanyActive(Company worker_company, Timestamp createdAt, Timestamp createdAt2, boolean worker_company_active);

    List<Contract> findByWorkerIdAndWorker_CompanyActive(Long worker_id, boolean worker_company_active);

    List<Contract> findByWorkerIdAndCreatedAtBetweenAndWorker_CompanyActive(Long worker_id, Timestamp createdAt, Timestamp createdAt2, boolean worker_company_active);

    List<Contract> findByCreatedAtBetween(Timestamp createdAt, Timestamp createdAt2);


    List<Contract> findByWorkerId(Long worker_id);

    List<Contract> findByWorker_CompanyId(Long worker_company_id);



}
