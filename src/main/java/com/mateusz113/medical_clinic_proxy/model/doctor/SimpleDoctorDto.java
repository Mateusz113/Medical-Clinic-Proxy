package com.mateusz113.medical_clinic_proxy.model.doctor;

public record SimpleDoctorDto(
        Long id,
        String email,
        String firstName,
        String lastName,
        String specialization
) {
}
