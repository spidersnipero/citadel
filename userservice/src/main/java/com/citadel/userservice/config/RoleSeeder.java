package com.citadel.userservice.config;


import com.citadel.userservice.repository.RoleRepo;
import com.citadel.userservice.model.Role;
import com.citadel.userservice.model.RoleName;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepo roleRepo;

    public RoleSeeder(RoleRepo roleRepo){
        this.roleRepo = roleRepo;
    }

    @Override
    public void run(String... args){
        for(RoleName roleName:RoleName.values()){
            roleRepo.findByName(roleName).orElseGet(()->{
               Role role = new Role(roleName);
               return roleRepo.save(role);
            });
        }
    }
}
