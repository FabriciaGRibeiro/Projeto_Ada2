package com.example.securityapp.service;

import com.example.securityapp.model.User;
import com.example.securityapp.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilizador não encontrado com e-mail: " + email));

        return new org.springframework.security.core.userdetails.User(
                (String) user.getEmail(),
                user.getPassword(),
                new ArrayList<>() // Sem roles por enquanto, pode ser expandido
        );
    }
}

