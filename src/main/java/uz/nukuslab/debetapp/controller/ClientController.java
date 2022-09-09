package uz.nukuslab.debetapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.nukuslab.debetapp.annotation.CheckRole;
import uz.nukuslab.debetapp.payload.ApiResponse;
import uz.nukuslab.debetapp.service.ClientService;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    ClientService clientService;

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN')")
    @GetMapping("/getAllClient")
    public HttpEntity<?> getAllClient(){
        ApiResponse apiResponse = clientService.getAll();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }



}
