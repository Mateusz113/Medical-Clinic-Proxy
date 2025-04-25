package com.mateusz113.medical_clinic_proxy.util;

import com.mateusz113.medical_clinic_proxy.model.PageableContentDto;
import com.mateusz113.medical_clinic_proxy.model.doctor.SimpleDoctorDto;
import com.mateusz113.medical_clinic_proxy.model.patient.PatientDto;
import com.mateusz113.medical_clinic_proxy.model.visit.VisitDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class VisitTestUtil {
    public static Pageable getPageable() {
        return PageRequest.of(0, 10);
    }

    public static VisitDto buildVisitDto(Long id) {
        return new VisitDto(
                id,
                getDefaultTime(),
                getDefaultTime().plusHours(1),
                new SimpleDoctorDto(id, "email", "firstName", "lastName", "specialization"),
                new PatientDto(id, "email", "idCardNo", "firstName", "lastName", "phoneNumber", LocalDate.of(2012, 12, 12)));
    }

    public static Clock getTestClock() {
        return Clock.fixed(Instant.parse("2012-12-12T12:00:00Z"), ZoneOffset.UTC);
    }

    public static OffsetDateTime getDefaultTime() {
        return OffsetDateTime.now(getTestClock());
    }

    public static PageableContentDto<VisitDto> buildPageableContent(Pageable pageable) {
        return PageableContentDto.<VisitDto>builder()
                .totalEntries(2)
                .totalNumberOfPages((int) Math.ceil((double) 2 / pageable.getPageSize()))
                .pageNumber(pageable.getPageNumber())
                .content(List.of(buildVisitDto(1L), buildVisitDto(2L)))
                .build();
    }
}
