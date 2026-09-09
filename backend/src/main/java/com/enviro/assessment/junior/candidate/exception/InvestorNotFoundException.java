package com.enviro.assessment.junior.candidate.exception;

/**
 * Thrown when an investor id supplied in a URL path does not exist.
 * Caught by GlobalExceptionHandler and translated into a 404 response.
 */
public class InvestorNotFoundException extends RuntimeException {

    public InvestorNotFoundException(Long investorId) {
        super("Investor not found with id: " + investorId);
    }
}
