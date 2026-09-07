package com.enviro.assessment.junior.candidate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Allows the React development server (running on a different port,
 * e.g. http://localhost:5173) to call this API from the browser.
 *
 * This is a local-development convenience only - not an authentication
 * or security mechanism. No auth is implemented per the assessment
 * brief, which explicitly says not to introduce unnecessary security
 * complexity.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    // Vite's default dev server port. Update this if you run the
    // frontend on a different port.
    private static final String FRONTEND_ORIGIN = "http://localhost:5173";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(FRONTEND_ORIGIN)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
