package com.enviro.assessment.junior.candidate.dto.request;

import com.enviro.assessment.junior.candidate.entity.WithdrawalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * What the frontend sends to POST /api/investors/{id}/withdrawals.
 *
 * This class only handles INPUT validation (is the request
 * well-formed?) via annotations. BUSINESS RULE validation (is this
 * withdrawal actually allowed for this investor?) happens afterwards
 * in WithdrawalService, because it depends on data - the investor's
 * age and current balance - that isn't available here.
 */
public class WithdrawalRequest {

    @NotNull(message = "Withdrawal type is required")
    private WithdrawalType withdrawalType;

    @NotNull(message = "Withdrawal amount is required")
    @Positive(message = "Withdrawal amount must be positive")
    private BigDecimal amount;

    protected WithdrawalRequest() {
        // Required for JSON deserialization
    }

    public WithdrawalRequest(WithdrawalType withdrawalType, BigDecimal amount) {
        this.withdrawalType = withdrawalType;
        this.amount = amount;
    }

    public WithdrawalType getWithdrawalType() {
        return withdrawalType;
    }

    public void setWithdrawalType(WithdrawalType withdrawalType) {
        this.withdrawalType = withdrawalType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
