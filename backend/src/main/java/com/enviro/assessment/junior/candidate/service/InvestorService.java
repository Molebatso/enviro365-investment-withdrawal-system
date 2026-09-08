package com.enviro.assessment.junior.candidate.service;

import com.enviro.assessment.junior.candidate.dto.response.InvestorResponse;
import com.enviro.assessment.junior.candidate.dto.response.PortfolioResponse;
import com.enviro.assessment.junior.candidate.entity.Investor;
import com.enviro.assessment.junior.candidate.entity.Portfolio;
import com.enviro.assessment.junior.candidate.exception.InvestorNotFoundException;
import com.enviro.assessment.junior.candidate.repository.InvestorRepository;
import com.enviro.assessment.junior.candidate.repository.PortfolioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Read-only queries for investor and portfolio data.
 * Kept separate from WithdrawalService since it has no business
 * rules to enforce - it only retrieves and maps data.
 */
@Service
public class InvestorService {

    private final InvestorRepository investorRepository;
    private final PortfolioRepository portfolioRepository;

    public InvestorService(InvestorRepository investorRepository, PortfolioRepository portfolioRepository) {
        this.investorRepository = investorRepository;
        this.portfolioRepository = portfolioRepository;
    }

    /**
     * Used by the frontend to populate the investor picker, since
     * there is no authentication/login in this assessment.
     */
    public List<InvestorResponse> getAllInvestors() {
        return investorRepository.findAll().stream()
                .map(InvestorResponse::fromEntity)
                .toList();
    }

    public InvestorResponse getInvestor(Long investorId) {
        Investor investor = findInvestorOrThrow(investorId);
        return InvestorResponse.fromEntity(investor);
    }

    public PortfolioResponse getPortfolio(Long investorId) {
        // Confirms the investor exists first, so a bad investor id
        // reliably returns 404 "investor not found" rather than a
        // less useful "portfolio not found" for the same root cause.
        findInvestorOrThrow(investorId);

        Portfolio portfolio = portfolioRepository.findByInvestorId(investorId)
                .orElseThrow(() -> new InvestorNotFoundException(investorId));

        return PortfolioResponse.fromEntity(portfolio);
    }

    private Investor findInvestorOrThrow(Long investorId) {
        return investorRepository.findById(investorId)
                .orElseThrow(() -> new InvestorNotFoundException(investorId));
    }
}
