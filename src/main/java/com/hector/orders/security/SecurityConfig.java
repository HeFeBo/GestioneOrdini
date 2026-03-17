package com.hector.orders.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.hector.orders.service.impl.AccountServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final AccountServiceImpl accountService;

    public SecurityConfig(AccountServiceImpl accountService) {
        this.accountService = accountService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(
                    "/api/customers/**", 
                                "/api/orders/**", 
                                "/api/products/**", 
                                "/api/orders-registration/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
            )
            .authenticationManager(authManager)
            .userDetailsService(accountService)
            .formLogin(form -> form.permitAll())
            .httpBasic(basic ->{});
        
        return http.build();
        }       

}
