package uz.nukuslab.debetapp.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import uz.nukuslab.debetapp.annotation.CheckRole;
import uz.nukuslab.debetapp.entity.Company;
import uz.nukuslab.debetapp.entity.Contract;
import uz.nukuslab.debetapp.entity.Debet;
import uz.nukuslab.debetapp.projection.DebetProjection;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin
@RepositoryRestResource(path = "debet", excerptProjection = DebetProjection.class)
public interface DebetRepository extends JpaRepository<Debet, Long> {

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    @Override
    <S extends Debet> List<S> saveAll(Iterable<S> entities);

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    @Override
    <S extends Debet> S save(S entity);

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    @Override
    Optional<Debet> findById(Long integer);

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @Override
    void deleteById(Long integer);

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @Override
    List<Debet> findAll();

    List<Debet> findByContractId(Long contract_id);

    Optional<Debet> findByContractIdAndId(Long contract_id, Long id);

//    List<Debet> findByContract_Worker_Company(Company contract_worker_company);

    List<Debet> findByContract_Worker_CompanyAndContract_Worker_CompanyActive(Company contract_worker_company, boolean contract_worker_company_active);

//    List<Report> findBySupervisorIdAndDateBetween(Long supervisor_id, Timestamp date, Timestamp date2);
//    List<Debet> findByContract_Worker_CompanyAndCreatedAtBetween(Company contract_worker_company, Timestamp createdAt, Timestamp createdAt2);

    List<Debet> findByContract_Worker_CompanyAndCreatedAtBetweenAndContract_Worker_CompanyActive(Company contract_worker_company, Timestamp createdAt, Timestamp createdAt2, boolean contract_worker_company_active);
//    List<Debet> findByCreatedAtBetweenAndContract_Worker_CompanyAndContract_Worker_CompanyActive(Timestamp createdAt, Timestamp createdAt2, Company contract_worker_company, boolean contract_worker_company_active);
//    List<Debet> findByContract_Worker_Company(Company contract_worker_company);


    List<Debet> findByContract_Worker_IdAndContract_IdAndPaid(Long contract_worker_id, Long contract_id, boolean paid);

//    findByWorkerIdAndCreatedAtBetweenAndWorker_CompanyActive
//    List<Debet> findByContract_Worker_IdAndUpdatedAtBetweenAndContract_Worker_Company_Active(Long contract_worker_id, Timestamp updatedAt, Timestamp updatedAt2, boolean contract_worker_company_active);
    List<Debet> findByContract_Worker_IdAndUpdatedAtBetweenAndContract_Worker_Company_ActiveAndPaid(Long contract_worker_id, Timestamp updatedAt, Timestamp updatedAt2, boolean contract_worker_company_active, boolean paid);

    List<Debet> findByPayDateBetweenAndPaidAndContract_Worker_Id(Timestamp payDate, Timestamp payDate2, boolean paid, Long contract_worker_id);

    List<Debet> findByPaidAndContract_Worker_IdAndPayDateBetweenOrderBy(boolean paid, Long contract_worker_id, Timestamp payDate, Timestamp payDate2);


}
