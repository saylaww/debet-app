package uz.nukuslab.debetapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.nukuslab.debetapp.DebetAppApplication;
import uz.nukuslab.debetapp.annotation.CheckRole;
import uz.nukuslab.debetapp.entity.User;
import uz.nukuslab.debetapp.payload.*;
import uz.nukuslab.debetapp.security.Paydalaniwshi;
import uz.nukuslab.debetapp.service.ContractService;

import javax.jws.soap.SOAPBinding;
import java.text.ParseException;

@CrossOrigin
@RestController
@RequestMapping("/contract")
public class ContractController {


    private final
    ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }


    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    @PostMapping("/byNumber")
    public HttpEntity<?> byNumber(@RequestParam String number,@Paydalaniwshi User user){
        ApiResponse apiResponse = contractService.byNumber(number, user);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    @PostMapping("/calc")
    public Object calc(@RequestBody Data data, @Paydalaniwshi User usr){

        ResponseData resData = new ResponseData(
                data.getPart(),
                data.getSumma() / 100 * data.getPercent() * data.getPart(),
                data.getSumma() / 100 * data.getPercent() + data.getSumma() / data.getPart(),
                data.getSumma() + data.getSumma() / 100 * data.getPercent() * data.getPart(),
                data.getSumma() / 100 * data.getPercent()
        );

        return resData;
    }

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    @PostMapping("/addContract")
    public HttpEntity<?> addContract(@RequestBody ContractDto contractDto){
        ApiResponse apiResponse = contractService.addContract(contractDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    @GetMapping("/findByClientPhone")
    public HttpEntity<?> findByClientPhone(@RequestBody ClientPhone cPhone){
        ApiResponse apiResponse = contractService.findByClientPhone(cPhone);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    @GetMapping("/getContractReportDayByCompany")
    public HttpEntity<?> getContractReportDayByCompany(@Paydalaniwshi User user){
        ApiResponse apiResponse = contractService.getContractReportDayByCompany(user);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    @GetMapping("/getContractReportMonthByCompany")
    public HttpEntity<?> getContractReportMonthByCompany(@RequestParam String monthNumber, @RequestParam String yearNumber, @Paydalaniwshi User user) throws ParseException {
        ApiResponse apiResponse = contractService.getContractReportMonthByCompany(user, monthNumber, yearNumber);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    @GetMapping("/getContractReportYearByCompany")
    public HttpEntity<?> getContractReportYearByCompany(@RequestParam Integer yearNumber, @Paydalaniwshi User user) throws ParseException {
        ApiResponse apiResponse = contractService.getContractReportYearByCompany(user, yearNumber);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    @GetMapping("/getMyContractReportDay")
    public HttpEntity<?> getMyContractReportDay(@RequestParam String dayNumber, @Paydalaniwshi User user) throws ParseException {
        ApiResponse apiResponse = contractService.getMyContractReportDay(user, dayNumber);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    @GetMapping("/getMyContractReportMonth")
    public HttpEntity<?> getMyContractReportMonth(@RequestParam String monthNumber, @RequestParam String yearNumber, @Paydalaniwshi User user) throws ParseException {
        ApiResponse apiResponse = contractService.getMyContractReportMonth(user, monthNumber, yearNumber);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    @GetMapping("/getMyContractReportYear")
    public HttpEntity<?> getMyContractReportYear(@RequestParam String yearNumber, @Paydalaniwshi User user) throws ParseException {
        ApiResponse apiResponse = contractService.getMyContractReportYear(user, yearNumber);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    @GetMapping("/getMyContract")
    public HttpEntity<?> getMyContract(@Paydalaniwshi User user){
        ApiResponse apiResponse = contractService.getMyContract(user);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @PostMapping("/getContractByCompanyId")
    public HttpEntity<?> getContractByCompanyId(@RequestParam Long id){
        ApiResponse apiResponse = contractService.getContractByCompanyId(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @PostMapping("/getAllContractByUserId")
    public HttpEntity<?> getAllContractByUserId(@RequestParam Long id){
        ApiResponse apiResponse = contractService.getAllContractByUserId(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN')")
    @GetMapping("/getAllContract")
    public HttpEntity<?> getAllContract(){
        ApiResponse apiResponse = contractService.getAll();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    @GetMapping("/getAllContractByNoPayed")
    public HttpEntity<?> getAllContractByNoPayed(@Paydalaniwshi User user){
        ApiResponse apiResponse = contractService.getAllContractByNoPayed(user);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    @GetMapping("/getAllContractByPayed")
    public HttpEntity<?> getAllContractByPayed(@Paydalaniwshi User user){
        ApiResponse apiResponse = contractService.getAllContractByPayed(user);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    @GetMapping("/getAllContractByClientAndUser")
    public HttpEntity<?> getAllContractList(@Paydalaniwshi User user){
        ApiResponse apiResponse = contractService.getAllContractList(user);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    @PostMapping("/getById")
    public HttpEntity<?> getById(@RequestParam Long id){
        ApiResponse apiResponse = contractService.getById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }









}
