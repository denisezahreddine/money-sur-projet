package com.bschool.moneysur.repository;

import com.bschool.moneysur.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByEmail(String email);

    // Utile pour vérifier si un email est déjà pris lors de l'inscription
    boolean existsByEmail(String email);

    Optional<User> findByResetToken(String resetToken);
}