package com.paymybuddy.config;

import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Actuator configuration class.
 *
 * @author Ludovic Tuccio
 */
@Configuration
public class ActuatorConfig {

    /**
     * Method used to configure httptrace endpoint with actuator.
     *
     * @return new InMemoryHttpTraceRepository
     */
    @Bean
    public HttpTraceRepository htttpTraceRepository() {
        return new InMemoryHttpTraceRepository();
    }

}
