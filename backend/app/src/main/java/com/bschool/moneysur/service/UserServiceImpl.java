package com.bschool.moneysur.service;


import com.bschool.moneysur.configuration.JwtUtil;
import com.bschool.moneysur.dto.UserLoginDto;
import com.bschool.moneysur.dto.UserRegistrationDto;
import com.bschool.moneysur.repository.UserRepository;
import com.bschool.moneysur.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public User register(UserRegistrationDto registrationDto) {
        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setRole(registrationDto.getRole());
        return userRepository.save(user);
    }

    public Optional<String> login(UserLoginDto loginDto) {
        Optional<User> userOptional = userRepository.findByUsername(loginDto.getUsername());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
                String token = jwtUtil.generateToken(user.getUsername());
                return Optional.of(token);
            }
        }
        return Optional.empty();
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }



}
