package uz.nukuslab.debetapp.service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.nukuslab.debetapp.entity.User;
import uz.nukuslab.debetapp.repository.UserRepository;

import java.util.Optional;

@Service
public class MyAuthService implements UserDetailsService {

    private final
    UserRepository userRepository;

    public MyAuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> byUsername = userRepository.findByUsername(username);
        if (byUsername.isPresent()) {
            User user = byUsername.get();
            return user;
        }
        return null;
    }


}
