package uz.nukuslab.debetapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.nukuslab.debetapp.entity.Company;
import uz.nukuslab.debetapp.entity.Contract;
import uz.nukuslab.debetapp.entity.Debet;
import uz.nukuslab.debetapp.entity.User;
import uz.nukuslab.debetapp.payload.ApiResponse;
import uz.nukuslab.debetapp.payload.DebetDto;
import uz.nukuslab.debetapp.payload.PayDto;
import uz.nukuslab.debetapp.repository.DebetRepository;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class DebetService {

    private final
    DebetRepository debetRepository;
    private final ContractService contractService;

    public DebetService(DebetRepository debetRepository, ContractService contractService) {
        this.debetRepository = debetRepository;
        this.contractService = contractService;
    }

    public ApiResponse setPay(PayDto payDto) {
        Optional<Debet> byContractIdAndId = debetRepository.findByContractIdAndId(payDto.getContractId(), payDto.getDebetId());
        if (!byContractIdAndId.isPresent()){
            return new ApiResponse("Bunday debet tabilmadi!!!", false);
        }
        Debet debet = byContractIdAndId.get();
        debet.setPaid(true);
        debetRepository.save(debet);
        contractService.checkDebetList(debet.getContract().getId());
        return new ApiResponse("To'lendi!!!", true);
    }


    public ApiResponse getAllDebetByMyCompany(User user) {
        List<Debet> debetList = debetRepository.findByContract_Worker_CompanyAndContract_Worker_CompanyActive(user.getCompany(), true);
        return new ApiResponse("Debet list", true, debetList);
    }

    public ApiResponse getDebetReportDayByCompany(User user) {
        Company company = user.getCompany();

        Timestamp start = new Timestamp(System.currentTimeMillis());
        start.setHours(0);
        start.setMinutes(0);
        start.setSeconds(1);

        Timestamp end = new Timestamp(System.currentTimeMillis());

        end.setHours(23);
        end.setMinutes(59);
        end.setSeconds(59);

        List<Debet> dayDebets = debetRepository.findByContract_Worker_CompanyAndCreatedAtBetweenAndContract_Worker_CompanyActive(company,start, end, true);
//        List<Debet> dayDebets = debetRepository.findByContract_Worker_Company(company);

        return new ApiResponse("Company boyinsha ku'nlik esabat!!!", true, dayDebets);
    }


    public ApiResponse getDebetByContractId(Long contractId, User user) {
        List<Debet> list = debetRepository.findByContract_Worker_IdAndContract_IdAndPaid(user.getId(), contractId,false);
        return new ApiResponse("debet list", true, list);
    }

    public ApiResponse getDebetByContractIdPayed(Long contractId, User user) {
        List<Debet> list = debetRepository.findByContract_Worker_IdAndContract_IdAndPaid(user.getId(), contractId,true);
        return new ApiResponse("debet list", true, list);
    }


    public ApiResponse updateDebet(Long id, boolean pay) {
        Optional<Debet> byId = debetRepository.findById(id);
        if (!byId.isPresent()){
            return new ApiResponse("Bunday id li debet tawilmadi!!!", false);
        }
        Debet debet = byId.get();
        debet.setPaid(pay);

        try {
            Debet save = debetRepository.save(debet);
            contractService.checkDebetList(debet.getContract().getId());
            return new ApiResponse("Debet updated", true, save);
        }catch (Exception e){
            return new ApiResponse("Error updated", false);
        }
    }

    public ApiResponse getMyAllDebetToNow(User user) {
        Timestamp createdAt = user.getCreatedAt();
        Timestamp now = new Timestamp(System.currentTimeMillis());

        List<Debet> list = debetRepository.findByContract_Worker_IdAndUpdatedAtBetweenAndContract_Worker_Company_ActiveAndPaid(
                user.getId(),
                createdAt,
                now,
                true,
                true
        );

        return new ApiResponse("Contract list", true, list);
    }

    //
    public ApiResponse getJournal(User user) {

        Timestamp start = new Timestamp(System.currentTimeMillis());
        start.setDate(1);
        start.setHours(0);
        start.setMinutes(0);
        start.setSeconds(0);

        int lastDay = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);

        Timestamp end = new Timestamp(System.currentTimeMillis());
        end.setDate(lastDay);
        end.setHours(23);
        end.setMinutes(59);
        end.setSeconds(59);

//        List<Debet> list = debetRepository.findByPayDateBetweenAndPaidAndContract_Worker_Id(
//                start,
//                end,
//                false,
//                user.getId()
//        );
        List<Debet> list = debetRepository.findByPaidAndContract_Worker_IdAndPayDateBetweenOrderByPayDate(
                false,
                user.getId(),
                start,
                end
        );
        if(list.isEmpty()){
            Timestamp nextMonth = new Timestamp(System.currentTimeMillis());
            int year = nextMonth.getYear()+1900;
            int month = nextMonth.getMonth()+1;
            int date = nextMonth.getDate();

        }

        return new ApiResponse("Jurnal listi", true, list);
    }


}
