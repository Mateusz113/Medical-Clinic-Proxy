package com.mateusz113.medical_clinic_proxy.service;

import com.mateusz113.medical_clinic_proxy.client.VisitClient;
import com.mateusz113.medical_clinic_proxy.model.PageableContentDto;
import com.mateusz113.medical_clinic_proxy.model.visit.VisitDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class VisitService {
    private final VisitClient visitClient;
    private final Clock clock;

    public PageableContentDto<VisitDto> getDoctorVisits(
            Long doctorId,
            Pageable pageable
    ) {
        return visitClient.getDoctorVisits(doctorId, pageable, true);
    }

    public PageableContentDto<VisitDto> getDoctorSpecializationVisits(
            String doctorSpecialization,
            Pageable pageable,
            LocalDate visitDate
    ) {
        OffsetDateTime startTime = OffsetDateTime.of(visitDate.getYear(), visitDate.getMonthValue(), visitDate.getDayOfMonth(), 0, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime endTime = startTime;
        endTime = endTime.plusDays(1);
        return visitClient.getDoctorSpecializationVisits(doctorSpecialization, pageable, true, startTime, endTime);
    }

    public PageableContentDto<VisitDto> getPatientVisits(
            Long patientId,
            Pageable pageable
    ) {
        return visitClient.getPatientVisits(patientId, pageable);
    }

    public void registerPatientToVisit(Long visitId, Long patientId) {
        visitClient.registerPatientToVisit(visitId, patientId);
    }
}
