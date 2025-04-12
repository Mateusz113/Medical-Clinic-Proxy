package com.mateusz113.medical_clinic_proxy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class MedicalClinicProxyConfig {
    @Bean
    public Clock getClock() {
        return Clock.systemUTC();
    }
}
