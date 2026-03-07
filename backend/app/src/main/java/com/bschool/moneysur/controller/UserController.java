package com.bschool.moneysur.controller;

import com.bschool.moneysur.dto.PinRequestDto;
import com.bschool.moneysur.dto.PinResponse;
import com.bschool.moneysur.dto.UserLoginDto;
import com.bschool.moneysur.dto.UserRegistrationDto;
import com.bschool.moneysur.repository.UserRepository;
import com.bschool.moneysur.service.UserService;
import com.bschool.moneysur.user.User;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.util.Map;
import java.util.Optional;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService,UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    //REGISTER
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto) {
        if (userService.existsByEmail(registrationDto.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Erreur : Email déjà utilisé !"));
        }

        userService.register(registrationDto);
        return ResponseEntity.ok(Map.of("message", "Inscription réussie ! Veuillez vérifier vos emails."));
    }

    //LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDto loginDto, HttpServletResponse response) {

        Optional<User> userOptional = userService.findByEmail(loginDto.getEmail());
        Optional<String> token = userService.login(loginDto);

        if (token.isPresent() && userOptional.isPresent()) {
            // Création du cookie
            ResponseCookie cookie =createAuthCookie(token.get());

            //On renvoie l'objet User complet
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(userOptional.get());
        } else {
            return ResponseEntity.status(401).body("Email ou mot de passe incorrect");
        }
    }

    //  VERIFY EMAIL (Auto-Login)
    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String token) {
        try {
            Optional<User> userOptional = userRepository.findByResetToken(token);
            Optional<String> jwt = userService.verifyEmailAndLogin(token);

            if (jwt.isPresent() && userOptional.isPresent()) {
                ResponseCookie cookie = createAuthCookie(jwt.get());
                return ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, cookie.toString())
                        .body(userOptional.get());
            } else {

                return ResponseEntity.badRequest().body(Map.of("message", "Lien invalide ou expiré."));
            }
        } catch (Exception e) {
            // Au cas où userService lance une exception si le token est déjà validé
            return ResponseEntity.badRequest().body(Map.of("message", "Ce lien a déjà été utilisé ou a expiré."));
        }
    }

    @GetMapping("/connected-user")
    public ResponseEntity<?> getConnectedUser(@AuthenticationPrincipal UserDetails userDetails) {

        // Si le JWT est absent ou invalide, userDetails sera null
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Aucune session active"));
        }

        // On récupère l'email depuis le token validé
        String email = userDetails.getUsername();

        // On va chercher les infos fraîches en base de données
        Optional<User> currentUser = userRepository.findByEmail(email);

        if (currentUser.isPresent()) {
            return ResponseEntity.ok(currentUser.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Utilisateur introuvable"));
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


    @PostMapping("/set-pin")
    public ResponseEntity<?> setPin(
            @Valid @RequestBody PinRequestDto pinRequestDto,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        userService.setPin(userDetails.getUsername(), pinRequestDto.getPin());

        return ResponseEntity.ok(Map.of("message", "Code PIN configuré avec succès !"));
    }

    @PostMapping("/verify-pin")
    public ResponseEntity<PinResponse> verifyPin(
            @Valid @RequestBody PinRequestDto pinRequestDto,
            @AuthenticationPrincipal UserDetails userDetails) {

        // On appelle le service qui renvoie l'objet PinResponse complet
        PinResponse response = userService.verifyPin(userDetails.getUsername(), pinRequestDto.getPin());

        // On détermine le code HTTP en fonction du message dans la réponse
        return switch (response.message()) {
            case "VALID" -> ResponseEntity.ok(response);

            case "LOCKED" -> ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(response);

            case "WRONG_PIN" -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(response);

            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        };
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