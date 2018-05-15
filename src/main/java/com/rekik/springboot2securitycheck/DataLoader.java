package com.rekik.springboot2securitycheck;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    AppRoleRepository appRoleRepository;

   // PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Loading data ...");

        appRoleRepository.save(new AppRole("USER"));
        appRoleRepository.save(new AppRole("ADMIN"));

        AppRole adminRole = appRoleRepository.findByRole("ADMIN");
        AppRole userRole = appRoleRepository.findByRole("USER");

        AppUser appUser = new AppUser("bob@bom.com","pass","Bob","Bobberson", true, "bob");
        appUser.setRoles(Arrays.asList(userRole));
        appUserRepository.save(appUser);

        appUser = new AppUser("jim@jim.com","pass","Jim","Jimmerson", true, "jim");
        appUser.setRoles(Arrays.asList(userRole));
        appUserRepository.save(appUser);

        appUser = new AppUser("admin@adm.com","pass","Admin","User", true, "admin");
        appUser.setRoles(Arrays.asList(adminRole));
        appUserRepository.save(appUser);
    }
}
