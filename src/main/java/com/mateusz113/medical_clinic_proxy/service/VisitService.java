package com.mateusz113.medical_clinic_proxy.service;

import com.mateusz113.medical_clinic_proxy.client.VisitClient;
import com.mateusz113.medical_clinic_proxy.filter.visit.ExternalVisitFilter;
import com.mateusz113.medical_clinic_proxy.filter.visit.InternalVisitFilter;
import com.mateusz113.medical_clinic_proxy.mapper.visit.InternalVisitFilterMapper;
import com.mateusz113.medical_clinic_proxy.model.PageableContentDto;
import com.mateusz113.medical_clinic_proxy.model.visit.VisitDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VisitService {
    private final VisitClient visitClient;
    private final InternalVisitFilterMapper internalVisitFilterMapper;

    public PageableContentDto<VisitDto> getVisits(InternalVisitFilter internalVisitFilter, Pageable pageable) {
        ExternalVisitFilter externalVisitFilter = internalVisitFilterMapper.toExternalFilter(internalVisitFilter);
        return visitClient.getVisits(externalVisitFilter, pageable);
    }

    public PageableContentDto<VisitDto> getPatientVisits(Long patientId, Pageable pageable) {
        return visitClient.getPatientVisits(patientId, pageable);
    }

    public void registerPatientToVisit(Long visitId, Long patientId) {
        visitClient.registerPatientToVisit(visitId, patientId);
    }
}
