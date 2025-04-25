package com.mateusz113.medical_clinic_proxy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mateusz113.medical_clinic_proxy.filter.visit.InternalVisitFilter;
import com.mateusz113.medical_clinic_proxy.service.VisitService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.mateusz113.medical_clinic_proxy.util.VisitTestUtil.buildPageableContent;
import static com.mateusz113.medical_clinic_proxy.util.VisitTestUtil.getPageable;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class VisitControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockitoBean
    private VisitService visitService;

    @Test
    void getVisits_ReturnsPageableContentDtoWithStatus200() throws Exception {
        Pageable pageable = getPageable();
        when(visitService.getVisits(InternalVisitFilter.builder().build(), pageable)).thenReturn(buildPageableContent(pageable));

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
    void getPatientVisits_ReturnsPageableContentDtoWithStatus200() throws Exception {
        Pageable pageable = getPageable();
        Long patientId = 1L;
        when(visitService.getPatientVisits(patientId, pageable)).thenReturn(buildPageableContent(pageable));

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
    void registerPatientToVisit_ReturnsStatus204() throws Exception {
        Long visitId = 1L;
        Long patientId = 1L;
        mockMvc.perform(patch("/visits/{visitId}/patient/{patientId}", visitId, patientId))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
