package com.rekik.springboot2securitycheck;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Transactional
@Service
public class SSUserDetailsService implements UserDetailsService {

    private AppUserRepository appUserRepository;

    public SSUserDetailsService(AppUserRepository appUserRepository){
        this.appUserRepository=appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{

        //For Database created users
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        try {
            AppUser appUser = appUserRepository.findByUsername(username);
            if (appUser == null) {
                return null;
            }

            return new User(
                    appUser.getUsername(),
                    //  encoder.encode For Database created users //remove this after creating users and in the
                    // database so that it doesn't encode their passwords twice
                  // encoder.encode(appUser.getPassword()),
                    appUser.getPassword(),
                    getAuthorities(appUser));
        }catch (Exception e){
            throw new  UsernameNotFoundException("User not found");
        }
    }


    private Set<GrantedAuthority> getAuthorities(AppUser appUser){
        Set<GrantedAuthority> authorities
                =new HashSet<GrantedAuthority>();
        for(AppRole appRole : appUser.getRoles()){
            GrantedAuthority grantedAuthority
                    = new SimpleGrantedAuthority(appRole.getRole());
            authorities.add(grantedAuthority);
        }
        return authorities;
    }
}