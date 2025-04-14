package com.mateusz113.medical_clinic_proxy.controller;

import com.mateusz113.medical_clinic_proxy.filter.visit.InternalVisitFilter;
import com.mateusz113.medical_clinic_proxy.model.PageableContentDto;
import com.mateusz113.medical_clinic_proxy.model.visit.VisitDto;
import com.mateusz113.medical_clinic_proxy.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/visits")
@RequiredArgsConstructor
public class VisitController {
    private final VisitService visitService;

    @GetMapping
    public PageableContentDto<VisitDto> getVisits(
            InternalVisitFilter internalVisitFilter,
            Pageable pageable
    ) {
        return visitService.getVisits(internalVisitFilter, pageable);
    }

    @GetMapping("/patient/{patientId}")
    public PageableContentDto<VisitDto> getPatientVisits(@PathVariable Long patientId, Pageable pageable) {
        return visitService.getPatientVisits(patientId, pageable);
    }

    @PatchMapping("/{visitId}/patient/{patientId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void registerPatientToVisit(@PathVariable("visitId") Long visitId, @PathVariable("patientId") Long patientId) {
        visitService.registerPatientToVisit(visitId, patientId);
    }
}
