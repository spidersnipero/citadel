package com.citadel.userservice.auth;

import com.citadel.userservice.auth.DTO.AuthRequest;
import com.citadel.userservice.auth.DTO.AuthResponse;
import com.citadel.userservice.model.Role;
import com.citadel.userservice.model.RoleName;
import com.citadel.userservice.model.Users;
import com.citadel.userservice.repository.RoleRepo;
import com.citadel.userservice.repository.UsersRepo;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Service
public class AuthService {

    private final UsersRepo usersRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final RoleRepo roleRepo;



    public AuthService(UsersRepo usersRepo,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService,
                       CustomUserDetailsService customUserDetailsService,
                       RoleRepo roleRepo ){
            this.usersRepo = usersRepo;
            this.passwordEncoder = passwordEncoder;
            this.authenticationManager = authenticationManager;
            this.jwtService = jwtService;
            this.customUserDetailsService = customUserDetailsService;
            this.roleRepo = roleRepo;
    }

    public String registerUser( AuthRequest user){
        if(usersRepo.existsByEmail(user.getEmail())) {
            throw new RuntimeException("User already exist");
        }
        Users newUser = new Users();
        newUser.setEmail(user.getEmail());
        newUser.setName(user.getName());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));

        Set<Role> rolesSet = new HashSet<>();
        for(String roleValue:user.getRoles()){
            RoleName roleName  = RoleName.valueOf(roleValue.toUpperCase());
            Optional<Role> role  = roleRepo.findByName(roleName);
            role.ifPresent(rolesSet::add);
        }
        newUser.setRoles(rolesSet);

        usersRepo.save(newUser);

        return "User registered successfully";
    }


    public AuthResponse authenticate(AuthRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
        UserDetails user = customUserDetailsService.loadUserByEmail(request.getEmail());
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return new AuthResponse(accessToken,refreshToken);
    }

    public AuthResponse refreshToken(String refreshToken){
        String email = jwtService.extractUserName(refreshToken);
        UserDetails user = customUserDetailsService.loadUserByEmail(email);
        if(!jwtService.isTokenValid(refreshToken,user)){
            throw new RuntimeException("Invalid refresh token");
        }
        String newAccessToken = jwtService.generateToken(user);
        return new AuthResponse(newAccessToken,refreshToken);
    }


}
