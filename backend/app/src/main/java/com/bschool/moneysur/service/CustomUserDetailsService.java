package com.bschool.moneysur.service;

import com.bschool.moneysur.repository.UserRepository;
import com.bschool.moneysur.user.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //  On cherche par email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // On construit l'objet UserDetails de Spring Security
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail()) // L'email sert d'identifiant (username) pour Spring Security
                .password(user.getPasswordHash())
                .roles(user.getTypeProfil()) // On utilise typeProfil
                .build();
    }
}