package com.mateusz113.medical_clinic_proxy.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.mateusz113.medical_clinic_proxy.exception.PatientNotRegisteredException;
import com.mateusz113.medical_clinic_proxy.model.PageableContentDto;
import com.mateusz113.medical_clinic_proxy.model.doctor.SimpleDoctorDto;
import com.mateusz113.medical_clinic_proxy.model.patient.PatientDto;
import com.mateusz113.medical_clinic_proxy.model.visit.VisitDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;
import org.wiremock.spring.InjectWireMock;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.mateusz113.medical_clinic_proxy.util.VisitTestUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@EnableWireMock(@ConfigureWireMock(name = "medical-clinic-client", port = 8080))
public class MedicalClinicClientTest {
    @InjectWireMock("medical-clinic-client")
    private WireMockServer server;
    @Autowired
    private MedicalClinicClient client;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getVisits_ReturnsPageableContentDto() throws JsonProcessingException {
        Pageable pageable = getPageable();
        String response = objectMapper.writeValueAsString(buildPageableContent(pageable));
        server.stubFor(get(urlPathEqualTo("/visits"))
                .withQueryParam("size", equalTo(String.valueOf(pageable.getPageSize())))
                .withQueryParam("page", equalTo(String.valueOf(pageable.getPageNumber())))
                .willReturn(okJson(response)));

        PageableContentDto<VisitDto> pageableContentDto = client.getVisits(null, pageable);

        assertEquals(2, pageableContentDto.totalEntries());
        assertEquals(1, pageableContentDto.totalNumberOfPages());
        assertEquals(0, pageableContentDto.pageNumber());
        assertEquals(2, pageableContentDto.content().size());
        for (int i = 0; i < pageableContentDto.content().size(); i++) {
            VisitDto visitDto = pageableContentDto.content().get(i);
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
    public void getVisits_SourceNotAvailable_ReturnsEmptyPageableContentDto() {
        Pageable pageable = getPageable();
        server.stubFor(get(urlPathEqualTo("/visits"))
                .withQueryParam("size", equalTo(String.valueOf(pageable.getPageSize())))
                .withQueryParam("page", equalTo(String.valueOf(pageable.getPageNumber())))
                .willReturn(aResponse().withStatus(503)));

        PageableContentDto<VisitDto> pageableContentDto = client.getVisits(null, pageable);

        verify(3, getRequestedFor(urlPathEqualTo("/visits"))
                .withQueryParam("size", equalTo(String.valueOf(pageable.getPageSize())))
                .withQueryParam("page", equalTo(String.valueOf(pageable.getPageNumber()))));
        assertEquals(0, pageableContentDto.totalEntries());
        assertEquals(0, pageableContentDto.totalNumberOfPages());
        assertEquals(0, pageableContentDto.pageNumber());
        assertEquals(0, pageableContentDto.content().size());
    }

    @Test
    public void getPatientVisits_ReturnsPageableContentDto() throws JsonProcessingException {
        Pageable pageable = getPageable();
        Long patientId = 1L;
        String response = objectMapper.writeValueAsString(buildPageableContent(pageable));
        server.stubFor(get(urlPathEqualTo("/visits"))
                .withQueryParam("size", equalTo(String.valueOf(pageable.getPageSize())))
                .withQueryParam("page", equalTo(String.valueOf(pageable.getPageNumber())))
                .withQueryParam("patientId", equalTo(String.valueOf(patientId)))
                .willReturn(okJson(response)));

        PageableContentDto<VisitDto> pageableContentDto = client.getPatientVisits(patientId, pageable);

        assertEquals(2, pageableContentDto.totalEntries());
        assertEquals(1, pageableContentDto.totalNumberOfPages());
        assertEquals(0, pageableContentDto.pageNumber());
        assertEquals(2, pageableContentDto.content().size());
        for (int i = 0; i < pageableContentDto.content().size(); i++) {
            VisitDto visitDto = pageableContentDto.content().get(i);
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
    public void getPatientVisits_SourceNotAvailable_ReturnsEmptyPageableContentDto() {
        Pageable pageable = getPageable();
        Long patientId = 1L;
        server.stubFor(get(urlPathEqualTo("/visits"))
                .withQueryParam("size", equalTo(String.valueOf(pageable.getPageSize())))
                .withQueryParam("page", equalTo(String.valueOf(pageable.getPageNumber())))
                .withQueryParam("patientId", equalTo(String.valueOf(patientId)))
                .willReturn(aResponse().withStatus(503)));

        PageableContentDto<VisitDto> pageableContentDto = client.getPatientVisits(patientId, pageable);

        verify(3, getRequestedFor(urlPathEqualTo("/visits"))
                .withQueryParam("size", equalTo(String.valueOf(pageable.getPageSize())))
                .withQueryParam("page", equalTo(String.valueOf(pageable.getPageNumber())))
                .withQueryParam("patientId", equalTo(String.valueOf(patientId))));
        assertEquals(0, pageableContentDto.totalEntries());
        assertEquals(0, pageableContentDto.totalNumberOfPages());
        assertEquals(0, pageableContentDto.pageNumber());
        assertEquals(0, pageableContentDto.content().size());
    }

    @Test
    public void registerPatientToVisit_ReturnsStatus204OnSuccess() {
        Long patientId = 1L;
        Long visitId = 1L;
        UrlPattern pattern = urlPathTemplate("/visits/{visitId}/patient/{patientId}");
        server.stubFor(patch(pattern)
                .withPathParam("visitId", equalTo(String.valueOf(visitId)))
                .withPathParam("patientId", equalTo(String.valueOf(patientId)))
                .willReturn(noContent()));

        client.registerPatientToVisit(visitId, patientId);

        verify(1, patchRequestedFor(pattern)
                .withPathParam("visitId", equalTo(String.valueOf(visitId)))
                .withPathParam("patientId", equalTo(String.valueOf(patientId))));
    }

    @Test
    public void registerPatientToVisit_RequestFailed_ThrowsPatientNotRegisteredException() {
        Long patientId = 1L;
        Long visitId = 1L;
        UrlPattern pattern = urlPathTemplate("/visits/{visitId}/patient/{patientId}");
        server.stubFor(patch(pattern)
                .withPathParam("visitId", equalTo(String.valueOf(visitId)))
                .withPathParam("patientId", equalTo(String.valueOf(patientId)))
                .willReturn(badRequest()));

        PatientNotRegisteredException exception = assertThrows(PatientNotRegisteredException.class, () -> client.registerPatientToVisit(visitId, patientId));

        verify(1, patchRequestedFor(pattern)
                .withPathParam("visitId", equalTo(String.valueOf(visitId)))
                .withPathParam("patientId", equalTo(String.valueOf(patientId))));
        assertEquals("Patient could not be registered.", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }
}
