package com.enviro.assessment.junior.candidate.dto.response;

import com.enviro.assessment.junior.candidate.entity.Investor;

/**
 * What the frontend sees for GET /api/investors/{id}.
 * age is included deliberately - the dashboard displays it, and it's
 * also the field the retirement-withdrawal rule depends on, so it's
 * useful for the frontend to know it up front.
 */
public class InvestorResponse {

    private final Long id;
    private final String firstName;
    private final String lastName;
    private final Integer age;
    private final String email;

    public InvestorResponse(Long id, String firstName, String lastName, Integer age, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
    }

    public static InvestorResponse fromEntity(Investor investor) {
        return new InvestorResponse(
                investor.getId(),
                investor.getFirstName(),
                investor.getLastName(),
                investor.getAge(),
                investor.getEmail()
        );
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Integer getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }
}
