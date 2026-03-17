package com.hector.orders.service.impl;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hector.orders.repository.AccountRepo;

@Service
public class AccountServiceImpl implements UserDetailsService{
    private final AccountRepo accountRepository;

    public AccountServiceImpl(AccountRepo accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var account = accountRepository.findByAccountName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato"));
        
        return new User(
            account.getAccountName(),
            account.getPassword(), 
            List.of(new SimpleGrantedAuthority("ROLE_" + account.getRole()))
        );

    }
}
