package com.rekik.springboot2securitycheck;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private SSUserDetailsService userDetailsService;

    @Autowired
    private AppUserRepository appUserRepository;

    @Bean
        //Using the bean for the password encoder, instead of putting it in the configure method.
        //The bean is always available in the context path once the application is run.
    PasswordEncoder encoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception{
        return new SSUserDetailsService(appUserRepository);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/","/h2-console/**","/register").permitAll()
               /* .access("hasAuthority('USER') or hasAuthority('ADMIN')")
                .antMatchers("/admin").access("hasAuthority('ADMIN')")*/
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").permitAll()
                .and()
                .logout()
                .logoutRequestMatcher(
                        new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login").permitAll().permitAll()
                .and()
                .httpBasic();

                http
                        .csrf().disable();
                http
                        .headers().frameOptions().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

       // PasswordEncoder encoder = new BCryptPasswordEncoder();
        auth.
                inMemoryAuthentication()
                .withUser("user")
                .password(encoder().encode("password")).authorities("USER")
                .and()
                .withUser("dave")
                .password(encoder().encode("begreat")).authorities("ADMIN")
                .and()
                .passwordEncoder(encoder());

        auth.
                userDetailsService(userDetailsServiceBean());

    }
}
