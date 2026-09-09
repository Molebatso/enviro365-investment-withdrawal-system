package com.enviro.assessment.junior.candidate.controller;

import com.enviro.assessment.junior.candidate.dto.response.InvestorResponse;
import com.enviro.assessment.junior.candidate.dto.response.PortfolioResponse;
import com.enviro.assessment.junior.candidate.service.InvestorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Thin controller - delegates everything to InvestorService and
 * returns whatever it produces. No business logic lives here.
 *
 * GET /api/investors                 -> all investors (for the picker)
 * GET /api/investors/{id}            -> investor details
 * GET /api/investors/{id}/portfolio  -> portfolio + investment products
 */
@RestController
@RequestMapping("/api/investors")
public class InvestorController {

    private final InvestorService investorService;

    public InvestorController(InvestorService investorService) {
        this.investorService = investorService;
    }

    @GetMapping
    public List<InvestorResponse> getAllInvestors() {
        return investorService.getAllInvestors();
    }

    @GetMapping("/{id}")
    public InvestorResponse getInvestor(@PathVariable Long id) {
        return investorService.getInvestor(id);
    }

    @GetMapping("/{id}/portfolio")
    public PortfolioResponse getPortfolio(@PathVariable Long id) {
        return investorService.getPortfolio(id);
    }
}
