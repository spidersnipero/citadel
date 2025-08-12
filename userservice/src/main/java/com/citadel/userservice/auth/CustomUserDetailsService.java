package com.citadel.userservice.auth;

import com.citadel.userservice.repository.UsersRepo;
import com.citadel.userservice.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService {

    @Autowired
    private UsersRepo usersRepo;

    public UserDetails loadUserByEmail(String email){
        Users user = usersRepo.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User with email "+email+" not found"));
        List<GrantedAuthority> authorityList = user.getRoles().stream().map(role->new SimpleGrantedAuthority("ROLE_"+role.getName().name())
        ).collect(Collectors.toUnmodifiableList());

        return new User(user.getEmail(), user.getPassword(), authorityList);
    }

}
