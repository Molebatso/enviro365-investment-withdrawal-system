package com.enviro.assessment.junior.candidate.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * A record of a withdrawal request. Only successful (APPROVED)
 * withdrawals are persisted - see WithdrawalService, which validates
 * the business rules before a WithdrawalNotice is ever saved.
 */
@Entity
@Table(name = "withdrawal_notice")
public class WithdrawalNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "investor_id", nullable = false)
    private Investor investor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WithdrawalType withdrawalType;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WithdrawalStatus status;

    @Column(nullable = false)
    private LocalDateTime requestedDate;

    private LocalDateTime processedDate;

    protected WithdrawalNotice() {
        // Required by JPA
    }

    public WithdrawalNotice(Investor investor, WithdrawalType withdrawalType, BigDecimal amount,
                             WithdrawalStatus status, LocalDateTime requestedDate, LocalDateTime processedDate) {
        this.investor = investor;
        this.withdrawalType = withdrawalType;
        this.amount = amount;
        this.status = status;
        this.requestedDate = requestedDate;
        this.processedDate = processedDate;
    }

    public Long getId() {
        return id;
    }

    public Investor getInvestor() {
        return investor;
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

    public void setStatus(WithdrawalStatus status) {
        this.status = status;
    }

    public LocalDateTime getRequestedDate() {
        return requestedDate;
    }

    public LocalDateTime getProcessedDate() {
        return processedDate;
    }

    public void setProcessedDate(LocalDateTime processedDate) {
        this.processedDate = processedDate;
    }
}
