package uz.nukuslab.debetapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.nukuslab.debetapp.DebetAppApplication;
import uz.nukuslab.debetapp.entity.*;
import uz.nukuslab.debetapp.payload.ApiResponse;
import uz.nukuslab.debetapp.payload.ClientPhone;
import uz.nukuslab.debetapp.payload.ContractDto;
import uz.nukuslab.debetapp.payload.DateDto;
import uz.nukuslab.debetapp.repository.*;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ContractService {

    private final
    ContractRepository contractRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final DebetRepository debetRepository;
    private final CompanyRepository companyRepository;
    @Autowired
    DebetAppApplication debetAppApplication;

    public ContractService(ContractRepository contractRepository, ProductRepository productRepository, UserRepository userRepository, ClientRepository clientRepository, DebetRepository debetRepository, CompanyRepository companyRepository) {
        this.contractRepository = contractRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.debetRepository = debetRepository;
        this.companyRepository = companyRepository;
    }

    public ApiResponse addContract(ContractDto contractDto, User user) {
//        Optional<Product> byProd = productRepository.findById(contractDto.getProductId());
//        if (!byProd.isPresent()){
//            return new ApiResponse("Bunday id li product tabilmadi!!!", false);
//        }
//        Product product = byProd.get();

//        Optional<User> byWorker = userRepository.findById(contractDto.getWorkerId());
//        if (!byWorker.isPresent()){
//            return new ApiResponse("Bunday id li Jumisshi tabilmadi!!!", false);
//        }
//        User user = byWorker.get();

        Optional<Client> byClient = clientRepository.findById(contractDto.getClientId());
        if (!byClient.isPresent()){
            return new ApiResponse("Bunday id li Client tabilmadi!!!", false);
        }
        Client client = byClient.get();

        Contract contract = new Contract(
                contractDto.getProductName(),
                user,
                contractDto.getPrice(),
                client,
                contractDto.getPercent(),
                contractDto.getPart()
        );

        Contract savedContract = contractRepository.save(contract);



        List<Debet> debetList = new ArrayList<>();

        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        int dayOfMonth = localDate.getDayOfMonth();
        int month = localDate.getMonthValue();
        int year = localDate.getYear();

        for (int i = 0; i < contractDto.getPart(); i++) {
            Month monthName = Month.of(month);
            if (month > 12){
                year++;
                month = 1;
            }
            debetList.add(new Debet(
                    dayOfMonth + " - " + monthName.toString() + " - " + year,
                    contractDto.getPrice() / 100 * savedContract.getPercent() + contractDto.getPrice() / contractDto.getPart(),
                    contract
            ));
            month++;
        }

        debetRepository.saveAll(debetList);

        return new ApiResponse("Contract saqlandi", true);
    }


    public void checkDebetList(Long contractId){
        int count = 0;
        List<Debet> debets = debetRepository.findByContractId(contractId);
        for (Debet debet : debets) {
            if (!debet.isPaid()){
                count++;
            }
        }
        if (count == 0){
            Optional<Contract> byId = contractRepository.findById(contractId);
            Contract contract = byId.get();
            contract.setEnabled(true);
            contractRepository.save(contract);
        }
    }

    public ApiResponse findByClientPhone(ClientPhone cPhone) {
        List<Contract> list = contractRepository.findByClient_Phone(cPhone.getPhone());
        return new ApiResponse("Contract list", true, list);
    }


    public ApiResponse getContractReportDayByCompany(User user) {
        Company company = user.getCompany();
        boolean check = checkCompany(company);
        if (!check){
            return new ApiResponse("Sizdin` kompaniyan'iz bloklang'an!!!", false);
        }

        Timestamp start = new Timestamp(System.currentTimeMillis());
        System.out.println(start.getMonth());

        start.setHours(0);
        start.setMinutes(0);
        start.setSeconds(1);

        Timestamp end = new Timestamp(System.currentTimeMillis());

        end.setHours(23);
        end.setMinutes(59);
        end.setSeconds(59);

        List<Contract> contracts = contractRepository.findByWorker_CompanyAndCreatedAtBetweenAndWorker_CompanyActive(company, start, end, true);
        return new ApiResponse("Menin' kompaniyamnin' ku'nlik contract lar dizimi!", true, contracts);
    }

    public ApiResponse getContractReportMonthByCompany(User user, String monthNumber,String yearNumber) throws ParseException {
        Company company = user.getCompany();

        boolean check = checkCompany(company);
        if (!check){
            return new ApiResponse("Sizdin` kompaniyan'iz bloklang'an!!!", false);
        }

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        String format = 1 + "/" + monthNumber + "/" + yearNumber;


        DateDto dateDto = new DateDto(format);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date date = dateFormat.parse(format);
        long time = date.getTime();
        Timestamp start = new Timestamp(time);

        System.out.println(start.getMonth());

        start.setDate(1);

        start.setHours(0);
        start.setMinutes(0);
        start.setSeconds(1);

        Timestamp end = new Timestamp(time);

        LocalDate monthValue = YearMonth.of(Integer.parseInt(yearNumber), Integer.parseInt(monthNumber)).atEndOfMonth();

        int lastDayOfMonth = monthValue.getDayOfMonth();
        end.setDate(lastDayOfMonth);

        end.setHours(23);
        end.setMinutes(59);
        end.setSeconds(59);

        List<Contract> monthContracts = contractRepository.findByWorker_CompanyAndCreatedAtBetweenAndWorker_CompanyActive(company, start, end, true);

        return new ApiResponse("Menin' kompaniyamnin' ayliq contractlar listi", true, monthContracts);
    }

    public boolean checkCompany(Company company){
        Optional<Company> byId = companyRepository.findById(company.getId());
        Company comp = byId.get();
        if (!comp.isActive()){
            return false;
        }
        return true;
    }

    public ApiResponse getContractReportYearByCompany(User user, Integer yearNumber) throws ParseException {
        Company company = user.getCompany();

        boolean check = checkCompany(company);
        if (!check){
            return new ApiResponse("Sizdin` kompaniyan'iz bloklang'an!!!", false);
        }

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        String format = 1 + "/" + 1 + "/" + yearNumber;


        DateDto dateDto = new DateDto(format);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date date = dateFormat.parse(format);
        long time = date.getTime();
        Timestamp start = new Timestamp(time);

        System.out.println(start.getMonth());

//        start.setDate(1);

        start.setHours(0);
        start.setMinutes(0);
        start.setSeconds(1);


        LocalDate monthValue = YearMonth.of(start.getYear(), 12).atEndOfMonth();

        int lastDayOfMonth = monthValue.getDayOfMonth();

        String formatEnd = lastDayOfMonth + "/" + 12 + "/" + yearNumber;


//        DateDto dateDto = new DateDto(format);
        DateFormat dateFormatEnd = new SimpleDateFormat("dd/MM/yyyy");

        Date dateEnd = dateFormatEnd.parse(formatEnd);
        long timeEnd = dateEnd.getTime();
        Timestamp end = new Timestamp(timeEnd);

        end.setHours(23);
        end.setMinutes(59);
        end.setSeconds(59);

        List<Contract> yearContracts = contractRepository.findByWorker_CompanyAndCreatedAtBetweenAndWorker_CompanyActive(company, start, end, true);

        return new ApiResponse("Menin' kompaniyamnin' jilliq(godovoy) contractlar listi", true, yearContracts);
    }

    public ApiResponse getMyContractReportDay(User user,String dayNumber) throws ParseException {
        Company company = user.getCompany();
        boolean check = checkCompany(company);
        if (!check){
            return new ApiResponse("Sizdin` kompaniyan'iz bloklang'an!!!", false);
        }

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        int month = timestamp.getMonth() + 1;
        int year = timestamp.getYear() + 1900;
        String format = dayNumber + "/" + month + "/" + year;

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date date = dateFormat.parse(format);
        long time = date.getTime();
        Timestamp start = new Timestamp(time);

        start.setHours(0);
        start.setMinutes(0);
        start.setSeconds(1);

        Timestamp end = new Timestamp(time);

        end.setHours(23);
        end.setMinutes(59);
        end.setSeconds(59);

        List<Contract> myDayReport = contractRepository.findByWorkerIdAndCreatedAtBetweenAndWorker_CompanyActive(user.getId(),start, end, true);

        return new ApiResponse("Menin' bir ku'nlik esabatim!", true, myDayReport);
    }


    public ApiResponse getMyContractReportMonth(User user, String montNumber, String yearNumber) throws ParseException {
        Company company = user.getCompany();

        boolean check = checkCompany(company);
        if (!check){
            return new ApiResponse("Sizdin` kompaniyan'iz bloklang'an!!!", false);
        }

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        int month = timestamp.getMonth() + 1;
        int year = timestamp.getYear() + 1900;

        String format = 1 + "/" + month + "/" + year;

        DateDto dateDto = new DateDto(format);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date date = dateFormat.parse(format);
        long time = date.getTime();
        Timestamp start = new Timestamp(time);

        System.out.println(start.getMonth());

//        start.setDate(1);

        start.setHours(0);
        start.setMinutes(0);
        start.setSeconds(1);


        LocalDate monthValue = YearMonth.of(Integer.parseInt(yearNumber), month).atEndOfMonth();

        int lastDayOfMonth = monthValue.getDayOfMonth();

        String formatEnd = lastDayOfMonth + "/" + month + "/" + year;

//        DateDto dateDto = new DateDto(format);
        DateFormat dateFormatEnd = new SimpleDateFormat("dd/MM/yyyy");

        Date dateEnd = dateFormatEnd.parse(formatEnd);
        long timeEnd = dateEnd.getTime();
        Timestamp end = new Timestamp(timeEnd);

        end.setHours(23);
        end.setMinutes(59);
        end.setSeconds(59);

        List<Contract> myDebets = contractRepository.findByWorkerIdAndCreatedAtBetweenAndWorker_CompanyActive(user.getId(), start, end, true);

        return new ApiResponse("Menin' ayliq esabatim!", true, myDebets);
    }

    public ApiResponse getMyContractReportYear(User user, String yearNumber) throws ParseException {
        Company company = user.getCompany();

        boolean check = checkCompany(company);
        if (!check){
            return new ApiResponse("Sizdin` kompaniyan'iz bloklang'an!!!", false);
        }

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
int year = timestamp.getYear() + 1900;
        int month = timestamp.getMonth() + 1;

        String format = 1 + "/" + 1 + "/" + yearNumber;

//        DateDto dateDto = new DateDto(format);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date date = dateFormat.parse(format);
        long time = date.getTime();
        Timestamp start = new Timestamp(time);

        start.setHours(0);
        start.setMinutes(0);
        start.setSeconds(1);

        LocalDate monthValue = YearMonth.of(start.getYear(), 12).atEndOfMonth();

        int lastDayOfMonth = monthValue.getDayOfMonth();

        String formatEnd = lastDayOfMonth + "/" + 12 + "/" + yearNumber;

        DateFormat dateFormatEnd = new SimpleDateFormat("dd/MM/yyyy");

        Date dateEnd = dateFormatEnd.parse(formatEnd);
        long timeEnd = dateEnd.getTime();
        Timestamp end = new Timestamp(timeEnd);

        end.setHours(23);
        end.setMinutes(59);
        end.setSeconds(59);

        List<Contract> myDebets = contractRepository.findByWorkerIdAndCreatedAtBetweenAndWorker_CompanyActive(user.getId(), start, end, true);

        return new ApiResponse("Menin' jilliq esabatim", true, myDebets);
    }


    public ApiResponse getMyContract(User user) {
        List<Contract> contracts = contractRepository.findByWorkerId(user.getId());
        return new ApiResponse("My contracts", true, contracts);
    }
}
