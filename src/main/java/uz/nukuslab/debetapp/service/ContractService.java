package uz.nukuslab.debetapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.nukuslab.debetapp.DebetAppApplication;
import uz.nukuslab.debetapp.entity.*;
import uz.nukuslab.debetapp.payload.*;
import uz.nukuslab.debetapp.repository.*;

import javax.jws.soap.SOAPBinding;
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

    public ApiResponse addContract(ContractDto contractDto) throws ParseException {

        Optional<Client> byClient = clientRepository.findById(contractDto.getClientId());
        if (!byClient.isPresent()) {
            return new ApiResponse("Bunday id li Client tabilmadi!!!", false);
        }
        Client client = byClient.get();


        Optional<User> byWorker = userRepository.findById(contractDto.getWorkerId());
        if (!byWorker.isPresent()) {
            return new ApiResponse("Bunday id li Jumisshi tabilmadi!!!", false);
        }
        User user = byWorker.get();

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
        int month = localDate.getMonthValue() + 1;
        int year = localDate.getYear();


        int count = 1;
        Timestamp timestamp = savedContract.getCreatedAt();

        int sDay = timestamp.getDate();
        int sMonth = timestamp.getMonth() + 1 + 1;
        int sYear = timestamp.getYear() + 1900;

        for (int i = 0; i < contractDto.getPart(); i++) {
            Month monthName = Month.of(month);

            String sane = sDay + "/" + sMonth + "/" + sYear;

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            Date date1 = dateFormat.parse(sane);
            long time = date1.getTime();
            Timestamp timestamp1 = new Timestamp(time);
            timestamp1.setHours(timestamp.getHours());
            timestamp1.setMinutes(timestamp.getMinutes());
            timestamp1.setSeconds(timestamp.getSeconds());


            System.out.println(timestamp.getMonth());
            debetList.add(new Debet(
                    dayOfMonth + " - " + monthName.toString() + " - " + year,
                    contractDto.getPrice() / 100 * savedContract.getPercent() + contractDto.getPrice() / contractDto.getPart(),
                    contract,
                    timestamp1
            ));

            boolean b = true;
            if (month == 12) {
                year++;
                month = 1;
                b = false;
                sMonth = 1;
                sYear++;
            }
            if (b) {
                month++;
                sMonth++;
            }
        }

        debetRepository.saveAll(debetList);

        return new ApiResponse("Contract saqlandi", true);
    }


    public void checkDebetList(Long contractId) {
        int count = 0;
        List<Debet> debets = debetRepository.findByContractId(contractId);
        for (Debet debet : debets) {
            if (!debet.isPaid()) {
                count++;
            }
        }
        if (count == 0) {
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
        if (!check) {
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

    public ApiResponse getContractReportMonthByCompany(User user, String monthNumber, String yearNumber) throws ParseException {
        Company company = user.getCompany();

        boolean check = checkCompany(company);
        if (!check) {
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

    public boolean checkCompany(Company company) {
        Optional<Company> byId = companyRepository.findById(company.getId());
        Company comp = byId.get();
        if (!comp.isActive()) {
            return false;
        }
        return true;
    }

    public ApiResponse getContractReportYearByCompany(User user, Integer yearNumber) throws ParseException {
        Company company = user.getCompany();

        boolean check = checkCompany(company);
        if (!check) {
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

    public ApiResponse getMyContractReportDay(User user, String dayNumber) throws ParseException {
        Company company = user.getCompany();
        boolean check = checkCompany(company);
        if (!check) {
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

        List<Contract> myDayReport = contractRepository.findByWorkerIdAndCreatedAtBetweenAndWorker_CompanyActive(user.getId(), start, end, true);

        return new ApiResponse("Menin' bir ku'nlik esabatim!", true, myDayReport);
    }


    public ApiResponse getMyContractReportMonth(User user, String montNumber, String yearNumber) throws ParseException {
        Company company = user.getCompany();

        boolean check = checkCompany(company);
        if (!check) {
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
        if (!check) {
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
//        List<Contract> contracts = contractRepository.findByWorkerIdOrderByCreatedAtDesc(user.getId());

        return new ApiResponse("My contracts", true, contracts);
    }

    public ApiResponse getAll() {
        List<Contract> all = contractRepository.findAll();
        return new ApiResponse("All contracts", true, all);
    }

    public ApiResponse getContractByCompanyId(Long id) {
        boolean b = companyRepository.existsById(id);
        if (!b) {
            return new ApiResponse("Bunday id li company bazada tabilmadi!!!", false);
        }
        List<Contract> list = contractRepository.findByWorker_CompanyId(id);

        return new ApiResponse("Contract list", true, list);
    }

    public ApiResponse getAllContractByUserId(Long id) {
        List<Contract> list = contractRepository.findByWorkerId(id);
        return new ApiResponse("Contract lis by User id", true, list);
    }


    public ApiResponse byNumber(String number, User user) {
//        List<Contract> list = contractRepository.findByClient_PhoneContainingAndWorker_Company_IdAndClient_Company_Id(
//                number,
//                user.getCompany().getId(),
//                user.getCompany().getId());
        List<Contract> list = contractRepository.findByClient_PhoneContainingAndWorker_Company_IdAndClient_Company_IdAndWorker_Id(
                number,
                user.getCompany().getId(),
                user.getCompany().getId(),
                user.getId()
        );
        return new ApiResponse("list", true, list);
    }

    public ApiResponse getAllContractByNoPayed(User user) {
//        List<Contract> list = contractRepository.findByEnabledAndClient_Company_IdAndWorker_Company_Id(
//                false,
//                user.getCompany().getId(),
//                user.getCompany().getId());
//        List<Contract> list = contractRepository.findByEnabledAndClient_Company_IdAndWorker_Company_IdOrderByCreatedAt(
//                false,
//                user.getCompany().getId(),
//                user.getCompany().getId());
        List<Contract> list = contractRepository.findByEnabledAndClient_Company_IdAndWorker_Company_IdAndWorker_IdOrderByCreatedAt(
                false,
                user.getCompany().getId(),
                user.getCompany().getId(),
                user.getId());
        return new ApiResponse("Contract List No Payed", true, list);
    }

    public ApiResponse getAllContractByPayed(User user) {
//        List<Contract> list = contractRepository.findByEnabledAndClient_Company_IdAndWorker_Company_Id(
//                true,
//                user.getCompany().getId(),
//                user.getCompany().getId());
//        List<Contract> list = contractRepository.findByEnabledAndClient_Company_IdAndWorker_Company_IdOrderByCreatedAt(
//                true,
//                user.getCompany().getId(),
//                user.getCompany().getId());
        List<Contract> list = contractRepository.findByEnabledAndClient_Company_IdAndWorker_Company_IdAndWorker_IdOrderByCreatedAt(
                true,
                user.getCompany().getId(),
                user.getCompany().getId(),
                user.getId());
        return new ApiResponse("Contract List Payed", true, list);
    }

    public ApiResponse getAllContractList(User user) {
//        List<Contract> list = contractRepository.findByClient_Company_IdAndWorker_Company_Id(user.getCompany().getId(), user.getCompany().getId());
//        List<Contract> list = contractRepository.findByClient_Company_IdAndWorker_Company_IdOrderByCreatedAt(user.getCompany().getId(), user.getCompany().getId());
        List<Contract> list = contractRepository.findByClient_Company_IdAndWorker_Company_IdAndWorker_IdOrderByCreatedAt(user.getCompany().getId(), user.getCompany().getId(), user.getId());
        return new ApiResponse("All Contract list", true, list);
    }

    public ApiResponse getById(Long id) {
        Optional<Contract> byId = contractRepository.findById(id);
        if (!byId.isPresent()) {
            return new ApiResponse("Bunday id li contract bazada tabilmadi!!!", false);
        }
        Contract contract = byId.get();
        return new ApiResponse("contract", true, contract);
    }

    public ApiResponse getMyAllContractToNow(User user) {
        Timestamp createdAt = user.getCreatedAt();
        Timestamp now = new Timestamp(System.currentTimeMillis());

        List<Contract> list = contractRepository.findByWorkerIdAndCreatedAtBetweenAndWorker_CompanyActive(
                user.getId(),
                createdAt,
                now,
                true
        );

        return new ApiResponse("Contract list", true, list);
    }

    public ApiResponse getMyAllContractBeetwen(User user, String start, String end) throws ParseException {
        if (start.equals("dan") || start.equals("")) {
//            start = user.getCreatedAt().toString().substring(0,9);
            start = user.getCreatedAt().getDate() + "/" + user.getCreatedAt().getMonth() + "/" + user.getCreatedAt().getYear();
            System.out.println(start);
        }

        SaneDto saneDto = new SaneDto(start, end);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date date = dateFormat.parse(saneDto.getStart());
        long time = date.getTime();
        Timestamp timestamp = new Timestamp(time);

        timestamp.setHours(0);
        timestamp.setMinutes(0);
        timestamp.setSeconds(1);


        Date date2 = dateFormat.parse(saneDto.getEnd());
        long time2 = date2.getTime();
        Timestamp timestamp2 = new Timestamp(time2);
        timestamp2.setHours(23);
        timestamp2.setMinutes(59);
        timestamp2.setSeconds(59);

        List<Contract> list = contractRepository.findByWorkerIdAndCreatedAtBetweenAndWorker_CompanyActive(
                user.getId(),
                timestamp,
                timestamp2,
                true
        );

        return new ApiResponse("Contract list by beetwen date", true, list);
    }


    public ApiResponse getMyAllDebetBeetwen(User user, String start, String end) throws ParseException {
        if (start.equals("dan") || start.equals("")) {
            start = user.getCreatedAt().getDate() + "/" + user.getCreatedAt().getMonth() + "/" + user.getCreatedAt().getYear();
            System.out.println(start);
        }

        SaneDto saneDto = new SaneDto(start, end);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date date = dateFormat.parse(saneDto.getStart());
        long time = date.getTime();
        Timestamp timestamp = new Timestamp(time);

        timestamp.setHours(0);
        timestamp.setMinutes(0);
        timestamp.setSeconds(1);


        Date date2 = dateFormat.parse(saneDto.getEnd());
        long time2 = date2.getTime();
        Timestamp timestamp2 = new Timestamp(time2);
        timestamp2.setHours(23);
        timestamp2.setMinutes(59);
        timestamp2.setSeconds(59);

        List<Debet> list = debetRepository.findByContract_Worker_IdAndUpdatedAtBetweenAndContract_Worker_Company_ActiveAndPaid(
                user.getId(),
                timestamp,
                timestamp2,
                true,
                true
        );

        return new ApiResponse("Debet list beetwen by date", true, list);
    }

    public ApiResponse addContractOld(ContractDto contractDto, String oldDate, Integer payedPart) throws ParseException {

        Optional<Client> byClient = clientRepository.findById(contractDto.getClientId());
        if (!byClient.isPresent()) {
            return new ApiResponse("Bunday id li Client tabilmadi!!!", false);
        }
        Client client = byClient.get();


        Optional<User> byWorker = userRepository.findById(contractDto.getWorkerId());
        if (!byWorker.isPresent()) {
            return new ApiResponse("Bunday id li Jumisshi tabilmadi!!!", false);
        }
        User user = byWorker.get();


//        if (start.equals("dan") || start.equals("")) {
//            start = user.getCreatedAt().getDate() + "/" + user.getCreatedAt().getMonth() + "/" + user.getCreatedAt().getYear();
//            System.out.println(start);
//        }

//        SaneDto saneDto = new SaneDto(start, end);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date date = dateFormat.parse(oldDate);
        long time = date.getTime();
        Timestamp timestampOld = new Timestamp(time);

        timestampOld.setHours(8);
        timestampOld.setMinutes(0);
        timestampOld.setSeconds(1);

        Contract contract = new Contract(
                contractDto.getProductName(),
                user,
                contractDto.getPrice(),
                client,
                contractDto.getPercent(),
                contractDto.getPart()
        );
        contract.setCreatedAt(timestampOld);

        Contract savedContract = contractRepository.save(contract);

        List<Debet> debetList = new ArrayList<>();

        Date dateCurrent = new Date();
        LocalDate localDate = dateCurrent.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        int dayOfMonth = localDate.getDayOfMonth();
        int month = localDate.getMonthValue() + 1;
        int year = localDate.getYear();


        int count = 1;
//        Timestamp timestamp = savedContract.getCreatedAt();

        int sDay = timestampOld.getDate();
        int sMonth = timestampOld.getMonth() + 1+1;
        int sYear = timestampOld.getYear() + 1900;

        int countPay=payedPart;
        for (int i = 0; i < contractDto.getPart(); i++) {
            Month monthName = Month.of(sMonth);

            String sane = sDay + "/" + sMonth + "/" + sYear;

            DateFormat dateFormatD = new SimpleDateFormat("dd/MM/yyyy");

            Date date1 = dateFormatD.parse(sane);
            long timeD = date1.getTime();
            Timestamp timestamp1 = new Timestamp(timeD);
            timestamp1.setHours(timestampOld.getHours());
            timestamp1.setMinutes(timestampOld.getMinutes());
            timestamp1.setSeconds(timestampOld.getSeconds());


//            System.out.println(timestamp.getMonth());
            Debet debet = new Debet(sDay + " - " + monthName.toString() + " - " + sYear,
                    contractDto.getPrice() / 100 * savedContract.getPercent() + contractDto.getPrice() / contractDto.getPart(),
                    contract,
                    timestamp1);
//            debetList.add(new Debet(
//                    sDay + " - " + monthName.toString() + " - " + sYear,
//                    contractDto.getPrice() / 100 * savedContract.getPercent() + contractDto.getPrice() / contractDto.getPart(),
//                    contract,
//                    timestamp1
//            ));
            if (count <=payedPart){
                debet.setPaid(true);
            }
            debetList.add(debet);

            boolean b = true;
            if (sMonth == 12) {
                year++;
//                sMonth = 1;
                b = false;
                sMonth = 1;
                sYear++;
            }
            if (b) {
//                month++;
                sMonth++;
            }
            count++;
        }


        debetRepository.saveAll(debetList);

        return new ApiResponse("Contract saqlandi", true);
    }
}
