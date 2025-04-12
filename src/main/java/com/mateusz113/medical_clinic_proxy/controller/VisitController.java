package com.mateusz113.medical_clinic_proxy.controller;

import com.mateusz113.medical_clinic_proxy.model.PageableContentDto;
import com.mateusz113.medical_clinic_proxy.model.visit.VisitDto;
import com.mateusz113.medical_clinic_proxy.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/visits")
@RequiredArgsConstructor
public class VisitController {
    private final VisitService visitService;

    @GetMapping("/doctor/id")
    public PageableContentDto<VisitDto> getDoctorVisits(
            @RequestParam Long doctorId,
            Pageable pageable
    ) {
        return visitService.getDoctorVisits(doctorId, pageable);
    }

    @GetMapping("/doctor/specialization")
    public PageableContentDto<VisitDto> getDoctorSpecializationVisits(
            @RequestParam String doctorSpecialization,
            Pageable pageable,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate visitDate
    ) {
        return visitService.getDoctorSpecializationVisits(doctorSpecialization, pageable, visitDate);
    }

    @GetMapping("/patient")
    PageableContentDto<VisitDto> getPatientVisits(@RequestParam Long patientId, Pageable pageable) {
        return visitService.getPatientVisits(patientId, pageable);
    }

    @PatchMapping("/{visitId}/patient")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void registerPatientToVisit(@PathVariable Long visitId, @RequestParam Long patientId) {
        visitService.registerPatientToVisit(visitId, patientId);
    }
}
