package com.bschool.moneysur.service;

import com.bschool.moneysur.configuration.JwtUtil;
import com.bschool.moneysur.dto.PinResponse;
import com.bschool.moneysur.dto.UserLoginDto;
import com.bschool.moneysur.dto.UserRegistrationDto;
import com.bschool.moneysur.repository.UserRepository;
import com.bschool.moneysur.user.User;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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


    @Override
    public void setPin(String email, String rawPin) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Sécurité : On hache le PIN avec BCrypt comme pour le mot de passe
        user.setPinHash(passwordEncoder.encode(rawPin));

        // On s'assure que les compteurs d'essais sont à zéro pour un nouveau PIN
        user.setPinAttempts(0);
        user.setPinLockedUntil(null);

        userRepository.save(user);
    }

    @Override
    @Transactional
    public PinResponse verifyPin(String email, String rawPin) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) return new PinResponse(false, "USER_NOT_FOUND", 0, null);

        User user = userOptional.get();
        LocalDateTime now = LocalDateTime.now();

        // GESTION DU RESET TEMPOREL (Nettoyage automatique)
        // Si une date de blocage existe et qu'elle est passée : on nettoie la BDD
        if (user.getPinLockedUntil() != null && user.getPinLockedUntil().isBefore(now)) {
            user.setPinLockedUntil(null);
            user.setPinAttempts(0);
        }

        //  VÉRIFICATION DU BLOCAGE ACTIF
        if (user.getPinLockedUntil() != null && user.getPinLockedUntil().isAfter(now)) {
            return new PinResponse(false, "LOCKED", 0, user.getPinLockedUntil());
        }

        // VÉRIFICATION DU PIN
        if (passwordEncoder.matches(rawPin, user.getPinHash())) {
            user.setPinAttempts(0);
            user.setPinLockedUntil(null);
            userRepository.save(user);
            return new PinResponse(true, "VALID", 3, null);
        } else {
            //  ÉCHEC : Le compteur repartira de 1 (grâce au reset ci-dessus si nécessaire)
            int newAttempts = user.getPinAttempts() + 1;
            user.setPinAttempts(newAttempts);

            if (newAttempts >= 3) {
                user.setPinLockedUntil(now.plusMinutes(1));//mettre 15 min apres
                userRepository.save(user);
                return new PinResponse(false, "LOCKED", 0, user.getPinLockedUntil());
            } else {
                userRepository.save(user);
                return new PinResponse(false, "WRONG_PIN", 3 - newAttempts, null);
            }
        }
    }
}


