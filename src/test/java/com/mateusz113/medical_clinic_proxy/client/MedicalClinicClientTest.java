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
import static org.assertj.core.api.Assertions.assertThat;
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

        assertThat(pageableContentDto.totalEntries()).isEqualTo(2);
        assertThat(pageableContentDto.totalNumberOfPages()).isEqualTo(1);
        assertThat(pageableContentDto.pageNumber()).isEqualTo(0);
        assertThat(pageableContentDto.content().size()).isEqualTo(2);
        for (int i = 0; i < pageableContentDto.content().size(); i++) {
            VisitDto visitDto = pageableContentDto.content().get(i);
            assertThat(visitDto.id()).isEqualTo(i + 1);
            assertThat(visitDto.startTime()).isEqualTo(getDefaultTime());
            assertThat(visitDto.endTime()).isEqualTo(getDefaultTime().plusHours(1));
            SimpleDoctorDto doctor = visitDto.doctor();
            assertThat(doctor.id()).isEqualTo(i + 1);
            assertThat(doctor.email()).isEqualTo("email");
            assertThat(doctor.firstName()).isEqualTo("firstName");
            assertThat(doctor.lastName()).isEqualTo("lastName");
            assertThat(doctor.specialization()).isEqualTo("specialization");
            PatientDto patient = visitDto.patient();
            assertThat(patient.id()).isEqualTo(i + 1);
            assertThat(patient.email()).isEqualTo("email");
            assertThat(patient.idCardNo()).isEqualTo("idCardNo");
            assertThat(patient.firstName()).isEqualTo("firstName");
            assertThat(patient.lastName()).isEqualTo("lastName");
            assertThat(patient.phoneNumber()).isEqualTo("phoneNumber");
            assertThat(patient.birthday()).isEqualTo("2012-12-12");
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

        assertThat(pageableContentDto.totalEntries()).isEqualTo(0);
        assertThat(pageableContentDto.totalNumberOfPages()).isEqualTo(0);
        assertThat(pageableContentDto.pageNumber()).isEqualTo(0);
        assertThat(pageableContentDto.content().size()).isEqualTo(0);
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

        assertThat(pageableContentDto.totalEntries()).isEqualTo(2);
        assertThat(pageableContentDto.totalNumberOfPages()).isEqualTo(1);
        assertThat(pageableContentDto.pageNumber()).isEqualTo(0);
        assertThat(pageableContentDto.content().size()).isEqualTo(2);
        for (int i = 0; i < pageableContentDto.content().size(); i++) {
            VisitDto visitDto = pageableContentDto.content().get(i);
            assertThat(visitDto.id()).isEqualTo(i + 1);
            assertThat(visitDto.startTime()).isEqualTo(getDefaultTime());
            assertThat(visitDto.endTime()).isEqualTo(getDefaultTime().plusHours(1));
            SimpleDoctorDto doctor = visitDto.doctor();
            assertThat(doctor.id()).isEqualTo(i + 1);
            assertThat(doctor.email()).isEqualTo("email");
            assertThat(doctor.firstName()).isEqualTo("firstName");
            assertThat(doctor.lastName()).isEqualTo("lastName");
            assertThat(doctor.specialization()).isEqualTo("specialization");
            PatientDto patient = visitDto.patient();
            assertThat(patient.id()).isEqualTo(i + 1);
            assertThat(patient.email()).isEqualTo("email");
            assertThat(patient.idCardNo()).isEqualTo("idCardNo");
            assertThat(patient.firstName()).isEqualTo("firstName");
            assertThat(patient.lastName()).isEqualTo("lastName");
            assertThat(patient.phoneNumber()).isEqualTo("phoneNumber");
            assertThat(patient.birthday()).isEqualTo("2012-12-12");
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

        assertThat(pageableContentDto.totalEntries()).isEqualTo(0);
        assertThat(pageableContentDto.totalNumberOfPages()).isEqualTo(0);
        assertThat(pageableContentDto.pageNumber()).isEqualTo(0);
        assertThat(pageableContentDto.content().size()).isEqualTo(0);
    }

    @Test
    public void registerPatientToVisit_ReturnsStatus204OnSuccess() {
        Long patientId = 1L;
        Long visitId = 1L;
        UrlPattern pattern = urlPathTemplate("/visits/{visitId}/patient/{patientId}");
        server.stubFor(patch(pattern)
                .withPathParam("visitId", equalTo(String.valueOf(visitId)))
                .withPathParam("visitId", equalTo(String.valueOf(patientId)))
                .willReturn(noContent()));

        client.registerPatientToVisit(visitId, patientId);

        verify(1, patchRequestedFor(pattern)
                .withPathParam("visitId", equalTo(String.valueOf(visitId)))
                .withPathParam("visitId", equalTo(String.valueOf(patientId))));
    }

    @Test
    public void registerPatientToVisit_RequestFailed_ThrowsPatientNotRegisteredException() {
        Long patientId = 1L;
        Long visitId = 1L;
        UrlPattern pattern = urlPathTemplate("/visits/{visitId}/patient/{patientId}");
        server.stubFor(patch(pattern)
                .withPathParam("visitId", equalTo(String.valueOf(visitId)))
                .withPathParam("visitId", equalTo(String.valueOf(patientId)))
                .willReturn(badRequest()));

        PatientNotRegisteredException exception = assertThrows(PatientNotRegisteredException.class, () -> client.registerPatientToVisit(visitId, patientId));

        verify(1, patchRequestedFor(pattern)
                .withPathParam("visitId", equalTo(String.valueOf(visitId)))
                .withPathParam("visitId", equalTo(String.valueOf(patientId))));
        assertThat(exception.getMessage()).isEqualTo("Patient could not be registered.");
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
