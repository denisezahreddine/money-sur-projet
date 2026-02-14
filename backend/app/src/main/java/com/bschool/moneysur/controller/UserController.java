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

    //REGISTER
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto) {
        if (userService.existsByEmail(registrationDto.getEmail())) {
            return ResponseEntity.badRequest().body("Erreur : Email déjà utilisé !");
        }

        userService.register(registrationDto);
        return ResponseEntity.ok("Inscription réussie ! Veuillez vérifier vos emails.");
    }

    //LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDto loginDto, HttpServletResponse response) {
        Optional<String> token = userService.login(loginDto);

        if (token.isPresent()) {
            // Création du cookie moderne
            ResponseCookie cookie =createAuthCookie(token.get());

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body("Connexion réussie !");
        } else {
            return ResponseEntity.status(401).body("Email ou mot de passe incorrect");
        }
    }

    //  VERIFY EMAIL (Auto-Login)
    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String token) {
        Optional<String> jwt = userService.verifyEmailAndLogin(token);

        if (jwt.isPresent()) {
            //  on crée le MEME cookie que pour le login
            ResponseCookie cookie = createAuthCookie(jwt.get());

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body("Email vérifié avec succès ! Vous êtes connecté.");
        } else {
            return ResponseEntity.badRequest().body("Lien invalide ou expiré.");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {

        ResponseCookie deleteCookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .secure(false) // true en prod
                .path("/")
                .maxAge(0) // supprime immédiatement
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());

        return ResponseEntity.ok("Déconnexion réussie");
    }


    private ResponseCookie createAuthCookie(String token) {
        return ResponseCookie.from("token", token)
                .httpOnly(true)// Sécurise le cookie côté JS
                .secure(false) // false en local, true en prod
                .path("/")// Cookie accessible sur tout le site
                .maxAge(10 * 60 * 60) // 10 heures
                .sameSite("Lax") // SameSite pour Angular local ,Strict' ou 'None' en prod selon le besoin
                .build();
    }

}