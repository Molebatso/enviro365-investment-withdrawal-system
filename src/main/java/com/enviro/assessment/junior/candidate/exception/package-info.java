/**
 * Custom exceptions and centralized exception handling.
 *
 * GlobalExceptionHandler (@ControllerAdvice) catches exceptions thrown
 * anywhere in the controller/service layers and converts them into a
 * consistent JSON error response with an appropriate HTTP status code,
 * so raw stack traces never reach the frontend.
 */
package com.enviro.assessment.junior.candidate.exception;
