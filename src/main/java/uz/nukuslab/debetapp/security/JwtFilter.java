package uz.nukuslab.debetapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.nukuslab.debetapp.entity.User;
import uz.nukuslab.debetapp.repository.UserRepository;
import uz.nukuslab.debetapp.service.AuthService;
import uz.nukuslab.debetapp.service.MyAuthService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final
    JwtProvider jwtProvider;
    private final
    MyAuthService myAuthService;
    private final
    AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final
    AuthService authService;

    public JwtFilter(JwtProvider jwtProvider, MyAuthService myAuthService, @Lazy AuthenticationManager authenticationManager, UserRepository userRepository, @Lazy AuthService authService) {
        this.jwtProvider = jwtProvider;
        this.myAuthService = myAuthService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer")) {
            authorization = authorization.substring(7);
            String username = jwtProvider.getUsernameFromToken(authorization);
            if (username != null) {
//                User user = userRepository.findByUsername(username).get();
//                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//                   user.getUsername(),
//                   user.getPassword()
//                ));
//                System.out.println("www");

                UserDetails userDetails = myAuthService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                authService.test(userDetails);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }


}
