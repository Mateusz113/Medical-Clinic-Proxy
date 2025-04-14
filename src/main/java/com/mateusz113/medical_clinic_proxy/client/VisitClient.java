package com.mateusz113.medical_clinic_proxy.client;

import com.mateusz113.medical_clinic_proxy.filter.visit.ExternalVisitFilter;
import com.mateusz113.medical_clinic_proxy.model.PageableContentDto;
import com.mateusz113.medical_clinic_proxy.model.visit.VisitDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "visits-client", url = "medical-clinic:8080/visits")
public interface VisitClient {
    @GetMapping
    PageableContentDto<VisitDto> getVisits(@SpringQueryMap ExternalVisitFilter visitFilter, Pageable pageable);

    @GetMapping
    PageableContentDto<VisitDto> getPatientVisits(@RequestParam Long patientId, Pageable pageable);

    @PatchMapping("/{visitId}/patient/{patientId}")
    void registerPatientToVisit(@PathVariable("visitId") Long visitId, @PathVariable("patientId") Long patientId);
}
