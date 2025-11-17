package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC Configuration
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    // Spring Boot auto-configuration will handle view resolution and static resources
}