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
    private final EmailService emailService;

    public UserServiceImpl(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder,EmailService emailService) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    public User register(UserRegistrationDto registrationDto) {
        User user = new User();
        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        user.setEmail(registrationDto.getEmail());
        // Hachage du mot de passe
        user.setPasswordHash(passwordEncoder.encode(registrationDto.getPassword()));
        user.setTypeProfil(registrationDto.getTypeProfil()); // 'SENIOR' ou 'FAMILY'
       // Statut "Non vérifié"
        user.setIsEmailVerified(false);
        // Initialisation par défaut
        user.setBalance(BigDecimal.ZERO);
        user.setDailySpend(BigDecimal.ZERO);

        //  Génération du token de vérification
        String verificationToken = java.util.UUID.randomUUID().toString();
        user.setResetToken(verificationToken); // On réutilise ce champ
        user.setResetExpiresAt(java.time.LocalDateTime.now().plusHours(24));
        User savedUser = userRepository.save(user);

        emailService.sendVerificationEmail(savedUser.getEmail(), savedUser.getResetToken());

        return savedUser;
    }

    @Override
    public Optional<String> login(UserLoginDto loginDto) {
        // On cherche par Email
        Optional<User> userOptional = userRepository.findByEmail(loginDto.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            //Vérifier si le mot de passe est correct
            if (passwordEncoder.matches(loginDto.getPassword(), user.getPasswordHash())) {

                // Vérifier si l'email est validé
                if (!user.getIsEmailVerified()) {
                    return Optional.empty();
                }
                //Si tout est OK, on génère le token
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
    public Optional<String> verifyEmailAndLogin(String token) {
        // Chercher l'utilisateur par son token UUID
        Optional<User> userOptional = userRepository.findByResetToken(token);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            //  Vérifier si le token n'est pas expiré
            if (user.getResetExpiresAt().isAfter(java.time.LocalDateTime.now())) {

                // Activer l'utilisateur et nettoyer les champs de token
                user.setIsEmailVerified(true);
                user.setResetToken(null);
                user.setResetExpiresAt(null);

                userRepository.save(user);

                // Générer le JWT pour connecter l'utilisateur immédiatement
                return Optional.of(jwtUtil.generateToken(user.getEmail()));
            }
        }
        return Optional.empty(); // Token invalide ou expiré
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


}
