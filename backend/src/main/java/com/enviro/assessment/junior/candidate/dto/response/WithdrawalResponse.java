package com.enviro.assessment.junior.candidate.dto.response;

import com.enviro.assessment.junior.candidate.entity.WithdrawalNotice;
import com.enviro.assessment.junior.candidate.entity.WithdrawalStatus;
import com.enviro.assessment.junior.candidate.entity.WithdrawalType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * What the frontend sees after POST /api/investors/{id}/withdrawals
 * succeeds, and each row of GET /api/investors/{id}/withdrawals.
 *
 * remainingBalance is included so the frontend can immediately update
 * the dashboard without a second round-trip to re-fetch the portfolio.
 */
public class WithdrawalResponse {

    private final Long id;
    private final WithdrawalType withdrawalType;
    private final BigDecimal amount;
    private final WithdrawalStatus status;
    private final LocalDateTime requestedDate;
    private final LocalDateTime processedDate;
    private final BigDecimal remainingBalance;

    public WithdrawalResponse(Long id, WithdrawalType withdrawalType, BigDecimal amount, WithdrawalStatus status,
                               LocalDateTime requestedDate, LocalDateTime processedDate, BigDecimal remainingBalance) {
        this.id = id;
        this.withdrawalType = withdrawalType;
        this.amount = amount;
        this.status = status;
        this.requestedDate = requestedDate;
        this.processedDate = processedDate;
        this.remainingBalance = remainingBalance;
    }

    public static WithdrawalResponse fromEntity(WithdrawalNotice notice, BigDecimal remainingBalance) {
        return new WithdrawalResponse(
                notice.getId(),
                notice.getWithdrawalType(),
                notice.getAmount(),
                notice.getStatus(),
                notice.getRequestedDate(),
                notice.getProcessedDate(),
                remainingBalance
        );
    }

    public Long getId() {
        return id;
    }

    public WithdrawalType getWithdrawalType() {
        return withdrawalType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public WithdrawalStatus getStatus() {
        return status;
    }

    public LocalDateTime getRequestedDate() {
        return requestedDate;
    }

    public LocalDateTime getProcessedDate() {
        return processedDate;
    }

    public BigDecimal getRemainingBalance() {
        return remainingBalance;
    }
}
