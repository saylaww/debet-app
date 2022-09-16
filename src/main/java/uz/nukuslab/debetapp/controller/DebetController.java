package uz.nukuslab.debetapp.controller;

import org.hibernate.annotations.Check;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.nukuslab.debetapp.annotation.CheckRole;
import uz.nukuslab.debetapp.entity.Debet;
import uz.nukuslab.debetapp.entity.User;
import uz.nukuslab.debetapp.payload.ApiResponse;
import uz.nukuslab.debetapp.payload.DebetDto;
import uz.nukuslab.debetapp.payload.PayDto;
import uz.nukuslab.debetapp.security.Paydalaniwshi;
import uz.nukuslab.debetapp.service.ContractService;
import uz.nukuslab.debetapp.service.DebetService;

@CrossOrigin
@RestController
@RequestMapping("/debet")
public class DebetController {


    @Autowired
    ContractService contractService;
    @Autowired
    DebetService debetService;

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    @PostMapping("/setPay")
    public HttpEntity<?> setPay(@RequestBody PayDto payDto){
        ApiResponse apiResponse = debetService.setPay(payDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @GetMapping("/getAllDebetByMyCompany")
    public HttpEntity<?> getAllDebetByMyCompany(@Paydalaniwshi User user){
        ApiResponse apiResponse = debetService.getAllDebetByMyCompany(user);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @GetMapping("/getDebetReportDayByCompany")
    public HttpEntity<?> getDebetReportDayByCompany(@Paydalaniwshi User user){
        ApiResponse apiResponse = debetService.getDebetReportDayByCompany(user);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    @PostMapping("/getDebetByContractIdNoPayed")
    public HttpEntity<?> getDebetByContractIdNoPayed(@RequestParam Long contractId, @Paydalaniwshi User user){
        ApiResponse apiResponse = debetService.getDebetByContractId(contractId, user);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    @PostMapping("/getDebetByContractIdPayed")
    public HttpEntity<?> getDebetByContractIdPayed(@RequestParam Long contractId, @Paydalaniwshi User user){
        ApiResponse apiResponse = debetService.getDebetByContractIdPayed(contractId, user);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    @PutMapping("/updateDebet")
    public HttpEntity<?> updateDebet(@RequestParam Long id, @RequestBody DebetDto debetDto){
        ApiResponse apiResponse = debetService.updateDebet(id, debetDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


}
