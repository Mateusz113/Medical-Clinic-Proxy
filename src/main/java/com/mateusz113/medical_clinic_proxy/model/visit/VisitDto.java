package com.mateusz113.medical_clinic_proxy.model.visit;

import com.mateusz113.medical_clinic_proxy.model.doctor.SimpleDoctorDto;
import com.mateusz113.medical_clinic_proxy.model.patient.PatientDto;

import java.time.OffsetDateTime;

public record VisitDto(
        Long id,
        OffsetDateTime startTime,
        OffsetDateTime endTime,
        SimpleDoctorDto doctor,
        PatientDto patient
) {
}
