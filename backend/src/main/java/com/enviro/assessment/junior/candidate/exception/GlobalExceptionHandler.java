package com.enviro.assessment.junior.candidate.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Central place where every exception thrown by a controller or
 * service is converted into a consistent ErrorResponse with an
 * appropriate HTTP status - so raw Java stack traces never reach
 * the frontend (mandatory per the assessment brief).
 *
 * Each handler method corresponds to one category of failure:
 *  - InvestorNotFoundException      -> 404 Not Found
 *  - InvalidWithdrawalException     -> 400 Bad Request (business rule)
 *  - MethodArgumentNotValidException -> 400 Bad Request (input validation)
 *  - anything else (unexpected)     -> 500 Internal Server Error
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(InvestorNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleInvestorNotFound(InvestorNotFoundException ex,
                                                                 HttpServletRequest request) {
        ErrorResponse body = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(InvalidWithdrawalException.class)
    public ResponseEntity<ErrorResponse> handleInvalidWithdrawal(InvalidWithdrawalException ex,
                                                                  HttpServletRequest request) {
        ErrorResponse body = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * Triggered by @Valid failures on WithdrawalRequest (e.g. missing
     * amount, negative amount). Combines all field errors into one
     * readable message rather than exposing Spring's default verbose
     * validation error structure.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex,
                                                                 HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));

        ErrorResponse body = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * Safety net for anything unanticipated. Deliberately generic -
     * ex.getMessage() is NOT included here, since an unexpected
     * exception's message could leak internal details (SQL, file
     * paths, etc.). Full detail still goes to the server log.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedError(Exception ex, HttpServletRequest request) {
        logger.error("Unexpected error handling request {}", request.getRequestURI(), ex);

        ErrorResponse body = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "An unexpected error occurred. Please try again later.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
