package com.mateusz113.medical_clinic_proxy.controller;

import com.mateusz113.medical_clinic_proxy.filter.visit.InternalVisitFilter;
import com.mateusz113.medical_clinic_proxy.model.PageableContentDto;
import com.mateusz113.medical_clinic_proxy.model.visit.VisitDto;
import com.mateusz113.medical_clinic_proxy.service.VisitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/visits")
@RequiredArgsConstructor
@Tag(name = "Visit Operations")
public class VisitController {
    private final VisitService visitService;

    @Operation(summary = "Get all visits")
    @ApiResponse(
            responseCode = "200",
            description = "Returns paged visits"
    )
    @GetMapping
    public PageableContentDto<VisitDto> getVisits(
            @Parameter(name = "visitFilter", description = "Optional filters for visits") @RequestParam(required = false) InternalVisitFilter internalVisitFilter,
            Pageable pageable
    ) {
        log.info("Request received: GET /visits");
        return visitService.getVisits(internalVisitFilter, pageable);
    }

    @Operation(summary = "Get all patient visits")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Found patient visits",
                    useReturnTypeSchema = true
            )
    })
    @GetMapping("/patient/{patientId}")
    public PageableContentDto<VisitDto> getPatientVisits(
            @Parameter(description = "Id of the patient to get visits of") @PathVariable Long patientId,
            Pageable pageable
    ) {
        log.info("Request received: GET /visits/patient/{}", patientId);
        return visitService.getPatientVisits(patientId, pageable);
    }

    @Operation(summary = "Register patient to visit")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Patient registered to visit"
            )
    })
    @PatchMapping("/{visitId}/patient/{patientId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void registerPatientToVisit(
            @Parameter(description = "Id of the visit for the patient to be registered to") @PathVariable("visitId") Long visitId,
            @Parameter(description = "Id of the patient to be registered") @PathVariable("patientId") Long patientId
    ) {
        log.info("Request received: PATCH /visits/{}/patient/{}", visitId, patientId);
        visitService.registerPatientToVisit(visitId, patientId);
    }
}
