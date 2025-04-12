package com.mateusz113.medical_clinic_proxy.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    public Logger.Level getFeignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
