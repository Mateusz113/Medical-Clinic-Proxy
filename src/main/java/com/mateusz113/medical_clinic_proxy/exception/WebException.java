package com.mateusz113.medical_clinic_proxy.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
public abstract class WebException extends RuntimeException {
    private final HttpStatus status;
    private final OffsetDateTime time;

    public WebException(String message, HttpStatus status, OffsetDateTime time) {
        super(message);
        this.status = status;
        this.time = time;
    }
}
