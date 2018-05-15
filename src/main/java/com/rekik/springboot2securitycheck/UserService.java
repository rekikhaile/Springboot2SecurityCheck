package com.rekik.springboot2securitycheck;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserService {
    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    AppRoleRepository appRoleRepository;

    @Autowired
    public UserService(AppUserRepository appUserRepository){
        this.appUserRepository = appUserRepository;
    }

    public AppUser findByEmail(String email){
        return appUserRepository.findByEmail(email);
    }

    public Long countByEmail(String email){
        return appUserRepository.countByEmail(email);
    }
    public AppUser findByUsername(String username){
        return appUserRepository.findByUsername(username);
    }
    public void saveUser(AppUser appUser){
        appUser.setRoles(Arrays.asList(appRoleRepository.findByRole("USER")));
        appUser.setEnabled(true);
        appUserRepository.save(appUser);
    }
    public void saveAdmin(AppUser appUser){
        appUser.setRoles(Arrays.asList(appRoleRepository.findByRole("ADMIN")));
        appUser.setEnabled(true);
        appUserRepository.save(appUser);
    }
}
