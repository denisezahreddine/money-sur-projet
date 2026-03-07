package com.bschool.moneysur.dto;

import java.time.LocalDateTime;

public record PinResponse(
        boolean success,
        String message,
        Integer remainingAttempts,
        LocalDateTime lockedUntil
) {}
