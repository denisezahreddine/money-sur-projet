package com.bschool.moneysur.service;



import com.bschool.moneysur.dto.UserLoginDto;
import com.bschool.moneysur.dto.UserRegistrationDto;
import com.bschool.moneysur.user.User;

import java.util.Optional;

public interface UserService  {


    User register(UserRegistrationDto registrationDto);

    Optional<String> login(UserLoginDto loginDto);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<String> verifyEmailAndLogin(String token);
}
