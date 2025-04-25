package com.mateusz113.medical_clinic_proxy.filter.visit;

import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
public record InternalVisitFilter(
        Long visitId,
        Long doctorId,
        String doctorSpecialization,
        @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate visitDate
) {
}
