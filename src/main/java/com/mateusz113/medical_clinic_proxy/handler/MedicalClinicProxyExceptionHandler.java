package com.mateusz113.medical_clinic_proxy.handler;

import com.mateusz113.medical_clinic_proxy.exception.WebException;
import com.mateusz113.medical_clinic_proxy.model.ErrorMessage;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Clock;
import java.time.OffsetDateTime;

@ControllerAdvice
@RequiredArgsConstructor
public class MedicalClinicProxyExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(exception = WebException.class)
    public ResponseEntity<Object> handleWebException(WebException ex, WebRequest request) {
        ErrorMessage body = new ErrorMessage(ex.getMessage(), ex.getStatus(), ex.getTime());
        return handleExceptionInternal(ex, body, new HttpHeaders(), ex.getStatus(), request);
    }
}
