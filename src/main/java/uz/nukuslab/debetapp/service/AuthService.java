package uz.nukuslab.debetapp.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.nukuslab.debetapp.entity.User;
import uz.nukuslab.debetapp.payload.ApiResponse;
import uz.nukuslab.debetapp.payload.LoginDto;
import uz.nukuslab.debetapp.payload.ResDto;
import uz.nukuslab.debetapp.repository.UserRepository;
import uz.nukuslab.debetapp.security.JwtProvider;

import java.util.Iterator;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;

    public ApiResponse login(LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getUsername(),
                    loginDto.getPassword()
            ));
            User user = (User) authentication.getPrincipal();

            Iterator<? extends GrantedAuthority> iterator = user.getAuthorities().iterator();
            GrantedAuthority next = iterator.next();
            String roleName = next.getAuthority();

            String token = jwtProvider.generateToken(loginDto.getUsername(), user.getRole());

            ResDto resDto = new ResDto(roleName, token);
            return new ApiResponse("TOken", true, resDto);
        } catch (Exception e) {
            return new ApiResponse("Parol yamasa login qa`te", false);
        }
    }

    public void test(UserDetails userDetails){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        "sadmin",
                        "123"
                )
        );
        System.out.println(123);
    }

}
