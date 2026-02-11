package com.bschool.moneysur.controller;

import com.bschool.moneysur.dto.UserLoginDto;
import com.bschool.moneysur.dto.UserRegistrationDto;
import com.bschool.moneysur.service.UserService;
import com.bschool.moneysur.user.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto) {
        if (userService.existsByEmail(registrationDto.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        User newUser = userService.register(registrationDto);
        return ResponseEntity.ok(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDto loginDto) { // Utilise <?> ici
        Optional<String> token = userService.login(loginDto);

        if (token.isPresent()) {
            // En cas de succès : on renvoie l'objet AuthResponse
            return ResponseEntity.ok(new AuthResponse(token.get()));
        } else {
            // En cas d'échec : on renvoie une String (401 Unauthorized)
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }

    // Classe interne pour structurer la réponse JSON du Token
    public static class AuthResponse {
        private String token;

        public AuthResponse(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}