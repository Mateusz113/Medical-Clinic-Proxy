package com.mateusz113.medical_clinic_proxy.client;

import com.mateusz113.medical_clinic_proxy.exception.PatientNotRegisteredException;
import com.mateusz113.medical_clinic_proxy.filter.visit.ExternalVisitFilter;
import com.mateusz113.medical_clinic_proxy.model.PageableContentDto;
import com.mateusz113.medical_clinic_proxy.model.visit.VisitDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MedicalClinicClientFallback implements MedicalClinicClient {
    private final Clock clock;

    @Override
    public PageableContentDto<VisitDto> getVisits(ExternalVisitFilter visitFilter, Pageable pageable) {
        return PageableContentDto.<VisitDto>builder()
                .totalEntries(0)
                .totalNumberOfPages(0)
                .pageNumber(pageable.getPageNumber())
                .content(List.of())
                .build();
    }

    @Override
    public PageableContentDto<VisitDto> getPatientVisits(Long patientId, Pageable pageable) {
        return PageableContentDto.<VisitDto>builder()
                .totalEntries(0)
                .totalNumberOfPages(0)
                .pageNumber(pageable.getPageNumber())
                .content(List.of())
                .build();
    }

    @Override
    public void registerPatientToVisit(Long visitId, Long patientId) {
        throw new PatientNotRegisteredException("Patient could not be registered.", OffsetDateTime.now(clock));
    }
}
