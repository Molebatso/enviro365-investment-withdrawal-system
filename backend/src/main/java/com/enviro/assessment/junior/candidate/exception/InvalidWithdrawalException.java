package com.enviro.assessment.junior.candidate.exception;

/**
 * Thrown when a withdrawal request fails one of the BUSINESS RULES
 * (retirement age, balance limit, 90% cap) - as distinct from basic
 * input validation, which is handled separately by Jakarta Bean
 * Validation on WithdrawalRequest.
 *
 * Caught by GlobalExceptionHandler and translated into a 400 response
 * with the message set here shown directly to the investor.
 */
public class InvalidWithdrawalException extends RuntimeException {

    public InvalidWithdrawalException(String message) {
        super(message);
    }
}
