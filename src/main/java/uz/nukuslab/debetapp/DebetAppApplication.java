package uz.nukuslab.debetapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uz.nukuslab.debetapp.entity.Contract;
import uz.nukuslab.debetapp.entity.User;
import uz.nukuslab.debetapp.entity.enums.RoleName;
import uz.nukuslab.debetapp.payload.DateDto;
import uz.nukuslab.debetapp.repository.ContractRepository;
import uz.nukuslab.debetapp.repository.UserRepository;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootApplication
@EnableScheduling
//@Component
public class DebetAppApplication {

    @Autowired
    ContractRepository contractRepository;
    @Autowired
    UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(DebetAppApplication.class, args);
    }


//    @Scheduled(cron = "0 " + Constatns.SEND_MONTH_MINUTE + " " + Constatns.SEND_MONTH_HOUR + " * * *")
    @Scheduled(cron = "0 42 19 * * *")
    public void reportMonth() throws Exception {

        List<User> users = userRepository.findByRole_RoleNameOrRole_RoleName(RoleName.USER, RoleName.ADMIN);

        for (User user : users) {
            if (user.getBalance() < 50){
                user.setAccountNonExpired(false);
            }else {
                user.setBalance(user.getBalance() - 50);
            }
        }
        userRepository.saveAll(users);
    }

    public void checkBalance(){
        List<User> users = userRepository.findByRole_RoleNameOrRole_RoleName(RoleName.USER, RoleName.ADMIN);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        int date = timestamp.getDate();
        for (User user : users) {
            if (user.getBalance() < 50){
                user.setAccountNonExpired(false);
            }else {
                user.setBalance(user.getBalance() - 50);
            }
        }

        userRepository.saveAll(users);
    }


}
