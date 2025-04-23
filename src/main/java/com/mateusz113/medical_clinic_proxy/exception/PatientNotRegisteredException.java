package com.mateusz113.medical_clinic_proxy.exception;

import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

public class PatientNotRegisteredException extends WebException {
    public PatientNotRegisteredException(String message, OffsetDateTime time) {
        super(message, HttpStatus.BAD_REQUEST, time);
    }
}
