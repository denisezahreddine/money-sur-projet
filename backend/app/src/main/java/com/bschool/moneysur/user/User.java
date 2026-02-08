package com.bschool.moneysur.user;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long idUser;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, unique = true, length = 180)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "pin_hash")
    private String pinHash;

    @Column(columnDefinition = "TEXT")
    private String photo;

    @Column(name = "type_profil", nullable = false, length = 20)
    private String typeProfil;

    @Column(precision = 19, scale = 4)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "daily_limit", precision = 19, scale = 4)
    private BigDecimal dailyLimit;

    @Column(name = "daily_spend", precision = 19, scale = 4)
    private BigDecimal dailySpend = BigDecimal.ZERO;

    @Column(name = "pin_attempts")
    private Integer pinAttempts = 0;

    @Column(name = "is_email_verified")
    private Boolean isEmailVerified = false;

    @Column(name = "pin_locked_until")
    private LocalDateTime pinLockedUntil;

    @Column(name = "reset_token", length = 100)
    private String resetToken;

    @Column(name = "reset_expires_at")
    private LocalDateTime resetExpiresAt;

    // --- GETTERS AND SETTERS ---

    public Long getIdUser() { return idUser; }
    public void setIdUser(Long idUser) { this.idUser = idUser; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getPinHash() { return pinHash; }
    public void setPinHash(String pinHash) { this.pinHash = pinHash; }

    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }

    public String getTypeProfil() { return typeProfil; }
    public void setTypeProfil(String typeProfil) { this.typeProfil = typeProfil; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public BigDecimal getDailyLimit() { return dailyLimit; }
    public void setDailyLimit(BigDecimal dailyLimit) { this.dailyLimit = dailyLimit; }

    public BigDecimal getDailySpend() { return dailySpend; }
    public void setDailySpend(BigDecimal dailySpend) { this.dailySpend = dailySpend; }

    public Integer getPinAttempts() { return pinAttempts; }
    public void setPinAttempts(Integer pinAttempts) { this.pinAttempts = pinAttempts; }

    public Boolean getIsEmailVerified() { return isEmailVerified; }
    public void setIsEmailVerified(Boolean isEmailVerified) { this.isEmailVerified = isEmailVerified; }

    public LocalDateTime getPinLockedUntil() { return pinLockedUntil; }
    public void setPinLockedUntil(LocalDateTime pinLockedUntil) { this.pinLockedUntil = pinLockedUntil; }

    public String getResetToken() { return resetToken; }
    public void setResetToken(String resetToken) { this.resetToken = resetToken; }

    public LocalDateTime getResetExpiresAt() { return resetExpiresAt; }
    public void setResetExpiresAt(LocalDateTime resetExpiresAt) { this.resetExpiresAt = resetExpiresAt; }
}