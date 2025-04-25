package com.mateusz113.medical_clinic_proxy.service;

import com.mateusz113.medical_clinic_proxy.client.MedicalClinicClient;
import com.mateusz113.medical_clinic_proxy.exception.PatientIllegalDataException;
import com.mateusz113.medical_clinic_proxy.exception.VisitIllegalDataException;
import com.mateusz113.medical_clinic_proxy.filter.visit.ExternalVisitFilter;
import com.mateusz113.medical_clinic_proxy.filter.visit.InternalVisitFilter;
import com.mateusz113.medical_clinic_proxy.mapper.visit.InternalVisitFilterMapper;
import com.mateusz113.medical_clinic_proxy.model.PageableContentDto;
import com.mateusz113.medical_clinic_proxy.model.visit.VisitDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class VisitService {
    private final MedicalClinicClient medicalClinicClient;
    private final InternalVisitFilterMapper internalVisitFilterMapper;
    private final Clock clock;

    public PageableContentDto<VisitDto> getVisits(InternalVisitFilter internalVisitFilter, Pageable pageable) {
        ExternalVisitFilter externalVisitFilter = (internalVisitFilter != null) ? internalVisitFilterMapper.toExternalFilter(internalVisitFilter) : null;
        return medicalClinicClient.getVisits(externalVisitFilter, pageable);
    }

    public PageableContentDto<VisitDto> getPatientVisits(Long patientId, Pageable pageable) {
        validatePatientId(patientId);
        return medicalClinicClient.getPatientVisits(patientId, pageable);
    }

    public void registerPatientToVisit(Long visitId, Long patientId) {
        validateVisitId(visitId);
        validatePatientId(patientId);
        medicalClinicClient.registerPatientToVisit(visitId, patientId);
    }

    private void validateVisitId(Long visitId) {
        if (visitId == null) {
            throw new VisitIllegalDataException("Visit ID cannot be null.", OffsetDateTime.now(clock));
        }
    }

    private void validatePatientId(Long patientId) {
        if (patientId == null) {
            throw new PatientIllegalDataException("Patient ID cannot be null.", OffsetDateTime.now(clock));
        }
    }
}
