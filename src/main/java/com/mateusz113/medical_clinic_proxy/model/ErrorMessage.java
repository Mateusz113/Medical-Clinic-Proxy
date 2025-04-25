package com.mateusz113.medical_clinic_proxy.model;

import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

public record ErrorMessage(
        String message,
        HttpStatus status,
        OffsetDateTime errorTime
) {
}
