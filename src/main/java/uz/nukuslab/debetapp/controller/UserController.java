package uz.nukuslab.debetapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.nukuslab.debetapp.annotation.CheckRole;
import uz.nukuslab.debetapp.entity.User;
import uz.nukuslab.debetapp.payload.ApiResponse;
import uz.nukuslab.debetapp.payload.UserDto;
import uz.nukuslab.debetapp.security.Paydalaniwshi;
import uz.nukuslab.debetapp.service.UserService;

import javax.jws.soap.SOAPBinding;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;


    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @GetMapping("/getAllUser")
    public HttpEntity<?> getAllUser(){
        ApiResponse apiResponse = userService.getAllUser();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @PostMapping("/addUser")
    public HttpEntity<?> addUser(@RequestBody UserDto userDto, @Paydalaniwshi User user){
        ApiResponse apiResponse = userService.addUser(userDto, user);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @GetMapping("/getUsersMyCompany")
    public HttpEntity<?> getUsersMyCompany(@Paydalaniwshi User user){
        ApiResponse apiResponse = userService.getUsersMyCompany(user);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @PutMapping("/blockUser")
    public HttpEntity<?> blockUser(@RequestParam Long id, @Paydalaniwshi User user){
        ApiResponse apiResponse = userService.blockUser(user, id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN')")
    @PutMapping("/blockAllUser/{id}")
    public HttpEntity<?> blockAllUser(@PathVariable Long id, @RequestBody User user){
        ApiResponse apiResponse = userService.blockAllUser(user,id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','SUPER_ADMIN')")
    @PutMapping("/blockAdmin")
    public HttpEntity<?> blockAdmin(@RequestParam Long id){
        ApiResponse apiResponse = userService.blockAdmin(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','SUPER_ADMIN')")
    @PutMapping("/unBlockUser")
    public HttpEntity<?> unBlockUser(@RequestParam Long id, @Paydalaniwshi User user){
        ApiResponse apiResponse = userService.unBlockUser(user, id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckRole
    @PreAuthorize(value = "hasAuthority('SUPER_ADMIN')")
    @PutMapping("/unBlockAdmin")
    public HttpEntity<?> unBlockAdmin(@RequestParam Long id){
        ApiResponse apiResponse = userService.unBlockAdmin(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckRole
    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN')")
    @PutMapping("/addBalance")
    public HttpEntity<?> addBalance(@RequestParam Long workerId,
                                    @RequestParam double summa){
        ApiResponse apiResponse = userService.addBalance(workerId, summa);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }




}
