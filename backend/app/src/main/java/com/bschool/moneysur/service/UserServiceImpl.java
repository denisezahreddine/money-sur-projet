package com.bschool.moneysur.service;


import com.bschool.moneysur.configuration.JwtUtil;
import com.bschool.moneysur.dto.UserLoginDto;
import com.bschool.moneysur.dto.UserRegistrationDto;
import com.bschool.moneysur.repository.UserRepository;
import com.bschool.moneysur.user.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(UserRegistrationDto registrationDto) {
        User user = new User();
        // Adaptation aux nouveaux champs de l'entité User
        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        user.setEmail(registrationDto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(registrationDto.getPassword()));
        user.setTypeProfil(registrationDto.getTypeProfil()); // 'SENIOR' ou 'FAMILY'

        // Initialisation par défaut
        user.setBalance(BigDecimal.ZERO);
        user.setDailySpend(BigDecimal.ZERO);

        return userRepository.save(user);
    }

    @Override
    public Optional<String> login(UserLoginDto loginDto) {
        // On cherche maintenant par Email (plus sécurisé et standard)
        Optional<User> userOptional = userRepository.findByEmail(loginDto.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(loginDto.getPassword(), user.getPasswordHash())) {
                // On génère le token basé sur l'email
                String token = jwtUtil.generateToken(user.getEmail());
                return Optional.of(token);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


}
