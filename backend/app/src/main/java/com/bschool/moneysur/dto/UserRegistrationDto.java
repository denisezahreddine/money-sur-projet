// src/main/java/com/example/demo/dto/UserRegistrationDto.java
package com.bschool.moneysur.dto;

public class UserRegistrationDto {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String typeProfil; // 'SENIOR' ou 'FAMILY'

    // --- Getters and Setters ---

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTypeProfil() {
        return typeProfil;
    }

    public void setTypeProfil(String typeProfil) {
        this.typeProfil = typeProfil;
    }
}
