package uz.nukuslab.debetapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.nukuslab.debetapp.entity.Role;
import uz.nukuslab.debetapp.payload.ApiResponse;
import uz.nukuslab.debetapp.repository.RoleRepository;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    public ApiResponse getAllRole() {
        List<Role> all = roleRepository.findAll();
        return new ApiResponse("All role", true, all);
    }
}
