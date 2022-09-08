package uz.nukuslab.debetapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.nukuslab.debetapp.annotation.CheckRole;
import uz.nukuslab.debetapp.entity.User;
import uz.nukuslab.debetapp.entity.enums.RoleName;
import uz.nukuslab.debetapp.payload.ApiResponse;
import uz.nukuslab.debetapp.security.Paydalaniwshi;
import uz.nukuslab.debetapp.service.CompanyService;

import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    CompanyService companyService;

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN')")
    @PostMapping("/findById/{id}")
    public HttpEntity<?> findById(@PathVariable Long id){
        ApiResponse apiResponse = companyService.byId(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN')")
    @PutMapping("/blockCompany")
    public HttpEntity<?> blockCompany(@RequestParam Long companyId){
        ApiResponse apiResponse = companyService.blockCompany(companyId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN')")
    @PutMapping("/unBlockCompany")
    public HttpEntity<?> unBlockCompany(@RequestParam Long companyId){
        ApiResponse apiResponse = companyService.unBlockCompany(companyId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN')")
    @PostMapping("/getAllCompany")
    public HttpEntity<?> getAllCompany(){
        ApiResponse apiResponse = companyService.getAllCompany();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


}
