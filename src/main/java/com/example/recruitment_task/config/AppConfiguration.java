package com.example.recruitment_task.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for the application.
 */
@Configuration
public class AppConfiguration {

    /**
     * Creates a RestTemplate bean.
     *
     * @param builder the RestTemplateBuilder
     * @return a configured RestTemplate instance
     */
    @Bean
    public RestTemplate restTemplate (RestTemplateBuilder builder) {
        return builder.build();
    }

}
