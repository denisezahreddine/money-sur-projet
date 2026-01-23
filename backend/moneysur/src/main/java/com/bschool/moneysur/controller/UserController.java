// src/main/java/com/example/demo/controller/UserController.java
package com.bschool.moneysur.controller;


import com.bschool.moneysur.dto.UserLoginDto;
import com.bschool.moneysur.dto.UserRegistrationDto;
import com.bschool.moneysur.service.UserService;
import com.bschool.moneysur.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserRegistrationDto registrationDto) {
        Optional<User> existingUser = userService.findByUsername(registrationDto.getUsername());
        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body(null);
        } else {
            User newUser = userService.register(registrationDto);
            return ResponseEntity.ok(newUser);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserLoginDto loginDto) {
        Optional<String> token = userService.login(loginDto);
        return token.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(401).body("Invalid credentials"));
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {

        return ResponseEntity.status(401).body("Invalid credentials");
    }
}

