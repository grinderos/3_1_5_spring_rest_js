package ru.kata.spring.rest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import ru.kata.spring.rest.service.PasswordEncoder;

@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationSuccessHandler successUserHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    public WebSecurityConfig(AuthenticationSuccessHandler successUserHandler
            , AuthenticationFailureHandler authenticationFailureHandler) {
        this.successUserHandler = successUserHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() //отключаем защиту от межсайтовой подделки запросов
                .authorizeRequests()
                .antMatchers("/admin","/api/admin/**").hasRole("ADMIN")
                .antMatchers("/user", "/api/user/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()

                .and()
                .formLogin()
                .successHandler(successUserHandler)
                .failureHandler(authenticationFailureHandler)
                .loginPage("/login")
                .loginProcessingUrl("/process_login")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .permitAll();
    }

    @Bean
    public AuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//        daoAuthenticationProvider.setPasswordEncoder(PasswordEncoder.bCryptPasswordEncoder());
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }

    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}