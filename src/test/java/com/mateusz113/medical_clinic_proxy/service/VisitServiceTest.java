package com.mateusz113.medical_clinic_proxy.service;

import com.mateusz113.medical_clinic_proxy.client.MedicalClinicClient;
import com.mateusz113.medical_clinic_proxy.exception.PatientIllegalDataException;
import com.mateusz113.medical_clinic_proxy.exception.VisitIllegalDataException;
import com.mateusz113.medical_clinic_proxy.mapper.visit.InternalVisitFilterMapper;
import com.mateusz113.medical_clinic_proxy.model.PageableContentDto;
import com.mateusz113.medical_clinic_proxy.model.doctor.SimpleDoctorDto;
import com.mateusz113.medical_clinic_proxy.model.patient.PatientDto;
import com.mateusz113.medical_clinic_proxy.model.visit.VisitDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import static com.mateusz113.medical_clinic_proxy.util.VisitTestUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class VisitServiceTest {
    private VisitService visitService;
    private MedicalClinicClient client;

    @BeforeEach
    void setUp() {
        client = mock(MedicalClinicClient.class);
        visitService = new VisitService(client, Mappers.getMapper(InternalVisitFilterMapper.class), getTestClock());
    }

    @Test
    void getVisits_DataIsPassed_ReturnsPageableContentDto() {
        Pageable pageable = getPageable();
        PageableContentDto<VisitDto> content = buildPageableContent(pageable);
        when(client.getVisits(null, pageable)).thenReturn(content);

        PageableContentDto<VisitDto> result = visitService.getVisits(null, pageable);

        assertEquals(2, result.content().size());
        for (int i = 0; i < result.content().size(); i++) {
            VisitDto visitDto = result.content().get(i);
            assertEquals(i + 1, visitDto.id());
            assertEquals(getDefaultTime(), visitDto.startTime());
            assertEquals(getDefaultTime().plusHours(1), visitDto.endTime());
            SimpleDoctorDto doctor = visitDto.doctor();
            assertEquals(i + 1, doctor.id());
            assertEquals("email", doctor.email());
            assertEquals("firstName", doctor.firstName());
            assertEquals("lastName", doctor.lastName());
            assertEquals("specialization", doctor.specialization());
            PatientDto patient = visitDto.patient();
            assertEquals(i + 1, patient.id());
            assertEquals("email", patient.email());
            assertEquals("idCardNo", patient.idCardNo());
            assertEquals("firstName", patient.firstName());
            assertEquals("lastName", patient.lastName());
            assertEquals("phoneNumber", patient.phoneNumber());
            assertEquals("2012-12-12", patient.birthday().toString());
        }
    }

    @Test
    void getPatientVisits_PatientIdIsNull_ThrowsPatientIllegalDataException() {
        Pageable pageable = getPageable();

        PatientIllegalDataException exception = assertThrows(PatientIllegalDataException.class, () -> visitService.getPatientVisits(null, pageable));

        assertEquals("Patient ID cannot be null.", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(getDefaultTime(), exception.getTime());
    }

    @Test
    void getPatientVisits_CorrectDataIsPassed_ReturnsPageableContentDto() {
        Pageable pageable = getPageable();
        Long patientId = 1L;
        PageableContentDto<VisitDto> content = buildPageableContent(pageable);
        when(client.getPatientVisits(patientId, pageable)).thenReturn(content);

        PageableContentDto<VisitDto> result = visitService.getPatientVisits(patientId, pageable);

        for (int i = 0; i < result.content().size(); i++) {
            VisitDto visitDto = result.content().get(i);
            assertEquals(i + 1, visitDto.id());
            assertEquals(getDefaultTime(), visitDto.startTime());
            assertEquals(getDefaultTime().plusHours(1), visitDto.endTime());
            SimpleDoctorDto doctor = visitDto.doctor();
            assertEquals(i + 1, doctor.id());
            assertEquals("email", doctor.email());
            assertEquals("firstName", doctor.firstName());
            assertEquals("lastName", doctor.lastName());
            assertEquals("specialization", doctor.specialization());
            PatientDto patient = visitDto.patient();
            assertEquals(i + 1, patient.id());
            assertEquals("email", patient.email());
            assertEquals("idCardNo", patient.idCardNo());
            assertEquals("firstName", patient.firstName());
            assertEquals("lastName", patient.lastName());
            assertEquals("phoneNumber", patient.phoneNumber());
            assertEquals("2012-12-12", patient.birthday().toString());
        }
    }

    @Test
    void registerPatientToVisit_VisitIdIsNull_ThrowsVisitIllegalDataException() {
        Long patientId = 1L;

        VisitIllegalDataException exception = assertThrows(VisitIllegalDataException.class, () -> visitService.registerPatientToVisit(null, patientId));

        assertEquals("Visit ID cannot be null.", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(getDefaultTime(), exception.getTime());
    }

    @Test
    void registerPatientToVisit_PatientIdIsNull_ThrowsPatientIllegalDataException() {
        Long visitId = 1L;

        PatientIllegalDataException exception = assertThrows(PatientIllegalDataException.class, () -> visitService.registerPatientToVisit(visitId, null));

        assertEquals("Patient ID cannot be null.", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(getDefaultTime(), exception.getTime());
    }

    @Test
    void registerPatientToVisit_CorrectDataIsPassed_RegisterClientMethodIsCalled() {
        Long visitId = 1L;
        Long patientId = 1L;

        visitService.registerPatientToVisit(visitId, patientId);

        verify(client, times(1)).registerPatientToVisit(visitId, patientId);
    }
}
