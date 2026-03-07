package com.bschool.moneysur.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class PinRequestDto {
    @NotBlank(message = "Le PIN est obligatoire")
    @Pattern(regexp = "^\\d{4}$", message = "Le PIN doit contenir exactement 4 chiffres")
    private String pin;

    // Getters / Setters
    public String getPin() { return pin; }
    public void setPin(String pin) { this.pin = pin; }
}



