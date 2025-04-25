package com.mateusz113.medical_clinic_proxy.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;
import org.wiremock.spring.InjectWireMock;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.mateusz113.medical_clinic_proxy.util.VisitTestUtil.buildPageableContent;
import static com.mateusz113.medical_clinic_proxy.util.VisitTestUtil.getPageable;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EnableWireMock(@ConfigureWireMock(name = "medical-clinic-client", port = 8080))
public class MedicalClinicProxyIntegrationTest {
    @InjectWireMock("medical-clinic-client")
    private WireMockServer server;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getVisits_SourceWorks_ReturnsStatus200WithPageableContentDto() throws Exception {
        Pageable pageable = getPageable();
        String response = objectMapper.writeValueAsString(buildPageableContent(pageable));
        server.stubFor(WireMock.get(urlPathEqualTo("/visits"))
                .withQueryParam("size", equalTo(String.valueOf(pageable.getPageSize())))
                .withQueryParam("page", equalTo(String.valueOf(pageable.getPageNumber())))
                .willReturn(okJson(response)));

        mockMvc.perform(get("/visits")
                        .param("size", String.valueOf(pageable.getPageSize()))
                        .param("page", String.valueOf(pageable.getPageNumber())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalEntries").value(2))
                .andExpect(jsonPath("$.totalNumberOfPages").value(1))
                .andExpect(jsonPath("$.pageNumber").value(0))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].startTime").value("2012-12-12T12:00:00Z"))
                .andExpect(jsonPath("$.content[0].endTime").value("2012-12-12T13:00:00Z"))
                .andExpect(jsonPath("$.content[0].doctor.id").value(1))
                .andExpect(jsonPath("$.content[0].doctor.email").value("email"))
                .andExpect(jsonPath("$.content[0].doctor.firstName").value("firstName"))
                .andExpect(jsonPath("$.content[0].doctor.lastName").value("lastName"))
                .andExpect(jsonPath("$.content[0].doctor.specialization").value("specialization"))
                .andExpect(jsonPath("$.content[0].patient.id").value(1))
                .andExpect(jsonPath("$.content[0].patient.email").value("email"))
                .andExpect(jsonPath("$.content[0].patient.idCardNo").value("idCardNo"))
                .andExpect(jsonPath("$.content[0].patient.firstName").value("firstName"))
                .andExpect(jsonPath("$.content[0].patient.lastName").value("lastName"))
                .andExpect(jsonPath("$.content[0].patient.phoneNumber").value("phoneNumber"))
                .andExpect(jsonPath("$.content[0].patient.birthday").value("2012-12-12"))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].startTime").value("2012-12-12T12:00:00Z"))
                .andExpect(jsonPath("$.content[1].endTime").value("2012-12-12T13:00:00Z"))
                .andExpect(jsonPath("$.content[1].doctor.id").value(2))
                .andExpect(jsonPath("$.content[1].doctor.email").value("email"))
                .andExpect(jsonPath("$.content[1].doctor.firstName").value("firstName"))
                .andExpect(jsonPath("$.content[1].doctor.lastName").value("lastName"))
                .andExpect(jsonPath("$.content[1].doctor.specialization").value("specialization"))
                .andExpect(jsonPath("$.content[1].patient.id").value(2))
                .andExpect(jsonPath("$.content[1].patient.email").value("email"))
                .andExpect(jsonPath("$.content[1].patient.idCardNo").value("idCardNo"))
                .andExpect(jsonPath("$.content[1].patient.firstName").value("firstName"))
                .andExpect(jsonPath("$.content[1].patient.lastName").value("lastName"))
                .andExpect(jsonPath("$.content[1].patient.phoneNumber").value("phoneNumber"))
                .andExpect(jsonPath("$.content[1].patient.birthday").value("2012-12-12"));
    }

    @Test
    public void getVisits_SourceReturnsError_ReturnsStatus200WithEmptyPageableContentDto() throws Exception {
        Pageable pageable = getPageable();
        server.stubFor(WireMock.get(urlPathEqualTo("/visits"))
                .withQueryParam("size", equalTo(String.valueOf(pageable.getPageSize())))
                .withQueryParam("page", equalTo(String.valueOf(pageable.getPageNumber())))
                .willReturn(aResponse().withStatus(503)));

        mockMvc.perform(get("/visits")
                        .param("size", String.valueOf(pageable.getPageSize()))
                        .param("page", String.valueOf(pageable.getPageNumber())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalEntries").value(0))
                .andExpect(jsonPath("$.totalNumberOfPages").value(0))
                .andExpect(jsonPath("$.pageNumber").value(0))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    public void getPatientVisits_SourceWorks_ReturnsStatus200WithPageableContentDto() throws Exception {
        Pageable pageable = getPageable();
        Long patientId = 1L;
        server.stubFor(WireMock.get(urlPathEqualTo("/visits"))
                .withQueryParam("size", equalTo(String.valueOf(pageable.getPageSize())))
                .withQueryParam("page", equalTo(String.valueOf(pageable.getPageNumber())))
                .withQueryParam("patientId", equalTo(String.valueOf(patientId)))
                .willReturn(aResponse().withStatus(503)));

        mockMvc.perform(get("/visits/patient/1")
                        .param("size", String.valueOf(pageable.getPageSize()))
                        .param("page", String.valueOf(pageable.getPageNumber())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalEntries").value(0))
                .andExpect(jsonPath("$.totalNumberOfPages").value(0))
                .andExpect(jsonPath("$.pageNumber").value(0))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    public void getPatientVisits_SourceReturnsError_ReturnsStatus200WithEmptyPageableContentDto() throws Exception {
        Pageable pageable = getPageable();
        Long patientId = 1L;
        String response = objectMapper.writeValueAsString(buildPageableContent(pageable));
        server.stubFor(WireMock.get(urlPathEqualTo("/visits"))
                .withQueryParam("size", equalTo(String.valueOf(pageable.getPageSize())))
                .withQueryParam("page", equalTo(String.valueOf(pageable.getPageNumber())))
                .withQueryParam("patientId", equalTo(String.valueOf(patientId)))
                .willReturn(okJson(response)));

        mockMvc.perform(get("/visits/patient/1")
                        .param("size", String.valueOf(pageable.getPageSize()))
                        .param("page", String.valueOf(pageable.getPageNumber())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalEntries").value(2))
                .andExpect(jsonPath("$.totalNumberOfPages").value(1))
                .andExpect(jsonPath("$.pageNumber").value(0))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].startTime").value("2012-12-12T12:00:00Z"))
                .andExpect(jsonPath("$.content[0].endTime").value("2012-12-12T13:00:00Z"))
                .andExpect(jsonPath("$.content[0].doctor.id").value(1))
                .andExpect(jsonPath("$.content[0].doctor.email").value("email"))
                .andExpect(jsonPath("$.content[0].doctor.firstName").value("firstName"))
                .andExpect(jsonPath("$.content[0].doctor.lastName").value("lastName"))
                .andExpect(jsonPath("$.content[0].doctor.specialization").value("specialization"))
                .andExpect(jsonPath("$.content[0].patient.id").value(1))
                .andExpect(jsonPath("$.content[0].patient.email").value("email"))
                .andExpect(jsonPath("$.content[0].patient.idCardNo").value("idCardNo"))
                .andExpect(jsonPath("$.content[0].patient.firstName").value("firstName"))
                .andExpect(jsonPath("$.content[0].patient.lastName").value("lastName"))
                .andExpect(jsonPath("$.content[0].patient.phoneNumber").value("phoneNumber"))
                .andExpect(jsonPath("$.content[0].patient.birthday").value("2012-12-12"))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].startTime").value("2012-12-12T12:00:00Z"))
                .andExpect(jsonPath("$.content[1].endTime").value("2012-12-12T13:00:00Z"))
                .andExpect(jsonPath("$.content[1].doctor.id").value(2))
                .andExpect(jsonPath("$.content[1].doctor.email").value("email"))
                .andExpect(jsonPath("$.content[1].doctor.firstName").value("firstName"))
                .andExpect(jsonPath("$.content[1].doctor.lastName").value("lastName"))
                .andExpect(jsonPath("$.content[1].doctor.specialization").value("specialization"))
                .andExpect(jsonPath("$.content[1].patient.id").value(2))
                .andExpect(jsonPath("$.content[1].patient.email").value("email"))
                .andExpect(jsonPath("$.content[1].patient.idCardNo").value("idCardNo"))
                .andExpect(jsonPath("$.content[1].patient.firstName").value("firstName"))
                .andExpect(jsonPath("$.content[1].patient.lastName").value("lastName"))
                .andExpect(jsonPath("$.content[1].patient.phoneNumber").value("phoneNumber"))
                .andExpect(jsonPath("$.content[1].patient.birthday").value("2012-12-12"));
    }

    @Test
    public void registerPatientToVisit_DataPassedIsCorrect_ReturnsStatus204() throws Exception {
        Long patientId = 1L;
        Long visitId = 1L;
        UrlPattern pattern = urlPathTemplate("/visits/{visitId}/patient/{patientId}");
        server.stubFor(WireMock.patch(pattern)
                .withPathParam("visitId", equalTo(String.valueOf(visitId)))
                .withPathParam("patientId", equalTo(String.valueOf(patientId)))
                .willReturn(noContent()));

        mockMvc.perform(patch("/visits/1/patient/1"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void registerPatientToVisit_RequestFailed_ReturnsStatus400WithBody() throws Exception {
        Long patientId = 1L;
        Long visitId = 1L;
        UrlPattern pattern = urlPathTemplate("/visits/{visitId}/patient/{patientId}");
        server.stubFor(WireMock.patch(pattern)
                .withPathParam("visitId", equalTo(String.valueOf(visitId)))
                .withPathParam("patientId", equalTo(String.valueOf(patientId)))
                .willReturn(badRequest()));
        mockMvc.perform(patch("/visits/1/patient/1"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Patient could not be registered."))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()));
    }
}
