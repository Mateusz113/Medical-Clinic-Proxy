package com.mateusz113.medical_clinic_proxy.client;

import com.mateusz113.medical_clinic_proxy.config.FeignConfig;
import com.mateusz113.medical_clinic_proxy.model.PageableContentDto;
import com.mateusz113.medical_clinic_proxy.model.visit.VisitDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.OffsetDateTime;

@FeignClient(value = "medical-clinic-visits", url = "medical-clinic:8080/visits")
public interface VisitClient {
    @GetMapping
    PageableContentDto<VisitDto> getDoctorVisits(
            @RequestParam Long doctorId,
            Pageable pageable,
            @RequestParam Boolean onlyAvailable
    );

    @GetMapping
    PageableContentDto<VisitDto> getDoctorSpecializationVisits(
            @RequestParam("doctorSpecialization") String doctorSpecialization,
            Pageable pageable,
            @RequestParam Boolean onlyAvailable,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endTime
    );

    @GetMapping
    PageableContentDto<VisitDto> getPatientVisits(@RequestParam Long patientId, Pageable pageable);

    @PatchMapping("/{visitId}/patient")
    void registerPatientToVisit(@PathVariable Long visitId, @RequestParam Long patientId);
}
