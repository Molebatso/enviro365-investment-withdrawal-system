package com.enviro.assessment.junior.candidate.controller;

import com.enviro.assessment.junior.candidate.dto.request.WithdrawalRequest;
import com.enviro.assessment.junior.candidate.dto.response.WithdrawalResponse;
import com.enviro.assessment.junior.candidate.service.WithdrawalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * POST /api/investors/{id}/withdrawals -> submit a withdrawal
 * GET  /api/investors/{id}/withdrawals -> withdrawal history for one investor
 *
 * @Valid triggers Jakarta Bean Validation on WithdrawalRequest (input
 * validation). Business rule validation happens inside WithdrawalService
 * and is surfaced as InvalidWithdrawalException, handled by
 * GlobalExceptionHandler.
 */
@RestController
@RequestMapping("/api/investors/{investorId}/withdrawals")
public class WithdrawalController {

    private final WithdrawalService withdrawalService;

    public WithdrawalController(WithdrawalService withdrawalService) {
        this.withdrawalService = withdrawalService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WithdrawalResponse createWithdrawal(@PathVariable Long investorId,
                                                @Valid @RequestBody WithdrawalRequest request) {
        return withdrawalService.createWithdrawal(investorId, request);
    }

    @GetMapping
    public List<WithdrawalResponse> getWithdrawalHistory(@PathVariable Long investorId) {
        return withdrawalService.getWithdrawalHistory(investorId);
    }
}
