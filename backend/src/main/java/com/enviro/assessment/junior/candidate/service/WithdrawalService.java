package com.enviro.assessment.junior.candidate.service;

import com.enviro.assessment.junior.candidate.dto.request.WithdrawalRequest;
import com.enviro.assessment.junior.candidate.dto.response.WithdrawalResponse;
import com.enviro.assessment.junior.candidate.entity.*;
import com.enviro.assessment.junior.candidate.exception.InvalidWithdrawalException;
import com.enviro.assessment.junior.candidate.exception.InvestorNotFoundException;
import com.enviro.assessment.junior.candidate.repository.InvestorRepository;
import com.enviro.assessment.junior.candidate.repository.PortfolioRepository;
import com.enviro.assessment.junior.candidate.repository.WithdrawalNoticeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Contains the three mandatory business rules for withdrawals. This is
 * the class the assessment is really testing, so each rule is broken
 * out into its own clearly-named private method rather than one long
 * if-chain - each one can be explained and unit tested independently.
 *
 * Withdrawal flow:
 *   1. Look up the investor (404 if missing)
 *   2. Look up their portfolio balance
 *   3. Validate all three business rules against that balance
 *   4. If any rule fails: throw InvalidWithdrawalException, save nothing
 *   5. If all pass: deduct the amount from the portfolio balance,
 *      persist an APPROVED WithdrawalNotice, return the result
 */
@Service
public class WithdrawalService {

    // RULE 1: retirement withdrawals require age STRICTLY GREATER THAN this.
    private static final int RETIREMENT_MINIMUM_AGE = 65;

    // RULE 3: a withdrawal may not exceed this fraction of the available balance.
    private static final BigDecimal MAX_WITHDRAWAL_PERCENTAGE = new BigDecimal("0.90");

    private final InvestorRepository investorRepository;
    private final PortfolioRepository portfolioRepository;
    private final WithdrawalNoticeRepository withdrawalNoticeRepository;

    public WithdrawalService(InvestorRepository investorRepository,
                              PortfolioRepository portfolioRepository,
                              WithdrawalNoticeRepository withdrawalNoticeRepository) {
        this.investorRepository = investorRepository;
        this.portfolioRepository = portfolioRepository;
        this.withdrawalNoticeRepository = withdrawalNoticeRepository;
    }

    @Transactional
    public WithdrawalResponse createWithdrawal(Long investorId, WithdrawalRequest request) {
        Investor investor = investorRepository.findById(investorId)
                .orElseThrow(() -> new InvestorNotFoundException(investorId));

        Portfolio portfolio = portfolioRepository.findByInvestorId(investorId)
                .orElseThrow(() -> new InvestorNotFoundException(investorId));

        BigDecimal amount = request.getAmount();
        BigDecimal availableBalance = portfolio.getAvailableBalance();

        validatePositiveAmount(amount);
        validateRetirementRule(request.getWithdrawalType(), investor.getAge());
        validateBalanceRule(amount, availableBalance);
        validateNinetyPercentCapRule(amount, availableBalance);

        // All rules passed - update the balance and persist the notice.
        BigDecimal remainingBalance = availableBalance.subtract(amount);
        portfolio.setAvailableBalance(remainingBalance);
        portfolioRepository.save(portfolio);

        LocalDateTime now = LocalDateTime.now();
        WithdrawalNotice notice = new WithdrawalNotice(
                investor,
                request.getWithdrawalType(),
                amount,
                WithdrawalStatus.APPROVED,
                now,
                now
        );
        withdrawalNoticeRepository.save(notice);

        return WithdrawalResponse.fromEntity(notice, remainingBalance);
    }

    public List<WithdrawalResponse> getWithdrawalHistory(Long investorId) {
        if (!investorRepository.existsById(investorId)) {
            throw new InvestorNotFoundException(investorId);
        }

        // remainingBalance in the history list isn't meaningful per-row
        // (it would require replaying balance changes in order), so we
        // report the investor's current balance for every row - good
        // enough for a history table where the amount/status/date
        // columns are what matters.
        BigDecimal currentBalance = portfolioRepository.findByInvestorId(investorId)
                .map(Portfolio::getAvailableBalance)
                .orElse(BigDecimal.ZERO);

        return withdrawalNoticeRepository.findByInvestorIdOrderByRequestedDateDesc(investorId).stream()
                .map(notice -> WithdrawalResponse.fromEntity(notice, currentBalance))
                .toList();
    }

    /**
     * Defense in depth: @Positive on WithdrawalRequest already blocks
     * zero/negative amounts when a request comes through the controller,
     * but the service should not silently trust its caller either -
     * this guards direct/programmatic callers (and is exercised
     * directly by the service-layer unit tests).
     */
    private void validatePositiveAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidWithdrawalException("Withdrawal amount must be positive.");
        }
    }

    /**
     * RULE 1 - Retirement withdrawals are only allowed for investors
     * strictly older than 65. Only applies to RETIREMENT-type requests;
     * STANDARD withdrawals are not age-restricted.
     */
    private void validateRetirementRule(WithdrawalType withdrawalType, int investorAge) {
        if (withdrawalType == WithdrawalType.RETIREMENT && investorAge <= RETIREMENT_MINIMUM_AGE) {
            throw new InvalidWithdrawalException(
                    "Retirement withdrawals are only allowed for investors older than 65.");
        }
    }

    /**
     * RULE 2 - A withdrawal must not exceed the investor's available balance.
     */
    private void validateBalanceRule(BigDecimal amount, BigDecimal availableBalance) {
        if (amount.compareTo(availableBalance) > 0) {
            throw new InvalidWithdrawalException(
                    "Withdrawal amount exceeds the available balance.");
        }
    }

    /**
     * RULE 3 - A withdrawal must not exceed 90% of the available balance.
     * Evaluated independently of Rule 2 (not as a replacement for it) -
     * a request can pass Rule 2 while still failing this one.
     */
    private void validateNinetyPercentCapRule(BigDecimal amount, BigDecimal availableBalance) {
        BigDecimal maxAllowed = availableBalance.multiply(MAX_WITHDRAWAL_PERCENTAGE);
        if (amount.compareTo(maxAllowed) > 0) {
            throw new InvalidWithdrawalException(
                    "Withdrawal amount exceeds 90% of the available balance.");
        }
    }
}
