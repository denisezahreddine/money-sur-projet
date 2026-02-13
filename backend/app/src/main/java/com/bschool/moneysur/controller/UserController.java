package com.bschool.moneysur.controller;

import com.bschool.moneysur.dto.UserLoginDto;
import com.bschool.moneysur.dto.UserRegistrationDto;
import com.bschool.moneysur.service.UserService;
import com.bschool.moneysur.user.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDto loginDto, HttpServletResponse response) {
        Optional<String> token = userService.login(loginDto);

        if (token.isPresent()) {
            // Création du cookie moderne
            ResponseCookie cookie = ResponseCookie.from("token", token.get())
                    .httpOnly(true)           // Sécurise le cookie côté JS
                    .secure(false)            // false en local, true en prod
                    .path("/")                // Cookie accessible sur tout le site
                    .maxAge(10 * 60 * 60)     // 10 heures
                    .sameSite("Lax")          // SameSite pour Angular local ,Strict' ou 'None' en prod selon le besoin
                    .build();

            // On ajoute le cookie à l'entête HTTP
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());


            return ResponseEntity.ok("Connexion réussie !");
        } else {
            return ResponseEntity.status(401).body("Email ou mot de passe incorrect");
        }
    }



}