package com.enviro.assessment.junior.candidate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Enviro365 Investment Withdrawal System.
 *
 * This is a Spring Boot application exposing a REST API used by the
 * React frontend to manage investor portfolios and withdrawal notices.
 *
 * On startup, Spring Boot will:
 *  1. Create the H2 database schema from the JPA entities (see application.properties).
 *  2. Run any seed data (see resources/data.sql).
 *  3. Start an embedded Tomcat server on port 8080.
 */
@SpringBootApplication
public class Enviro365Application {

    public static void main(String[] args) {
        SpringApplication.run(Enviro365Application.class, args);
    }
}
