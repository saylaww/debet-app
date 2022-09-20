package uz.nukuslab.debetapp.component;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.nukuslab.debetapp.entity.Company;
import uz.nukuslab.debetapp.entity.Role;
import uz.nukuslab.debetapp.entity.Rule;
import uz.nukuslab.debetapp.entity.User;
import uz.nukuslab.debetapp.entity.enums.RoleName;
import uz.nukuslab.debetapp.repository.CompanyRepository;
import uz.nukuslab.debetapp.repository.RoleRepository;
import uz.nukuslab.debetapp.repository.RuleRepository;
import uz.nukuslab.debetapp.repository.UserRepository;

import java.util.Collections;

@Component
public class DataLoader implements CommandLineRunner {

    @Value("${spring.sql.init.mode}")
    private String initialMode;

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    RuleRepository ruleRepository;


    @Override
    public void run(String... args) throws Exception {
        if (initialMode.equals("always")) {

            Role sAdmin = roleRepository.save(new Role(
                    RoleName.SUPER_ADMIN
            ));

            Role admin = roleRepository.save(new Role(
                    RoleName.ADMIN
            ));
            Role user = roleRepository.save(new Role(
                    RoleName.USER
            ));


//            klklkk
            Company nukuslab_llc = companyRepository.save(new Company(
                    "Nukuslab",
                    true
            ));
//
//            Company bazar = companyRepository.save(
//                    new Company(
//                            "Bazar",
//                            true
//                    )
//            );


            userRepository.save(new User(
                    "Saylaubay",
                    "Bekmurzaev",
                    "sadmin",
                    passwordEncoder.encode("123"),
                    "+998974748061",
                    nukuslab_llc,
                    sAdmin,
                    true,
                    true,
                    true,
                    true
            ));

//            userRepository.save(new User(
//                    "Jaras",
//                    "Amirov",
//                    "user",
//                    passwordEncoder.encode("456"),
//                    "+123456789",
////                    bazar,
//                    user
//            ));
//            userRepository.save(new User(
//                    "Orash",
//                    "Qutlimuratov",
//                    "admin",
//                    passwordEncoder.encode("789"),
//                    "+897456213",
////                    bazar,
//                    admin
//            ));

            ruleRepository.save(new Rule(
                   100000,
                   200000
            ));



        }
    }

}
