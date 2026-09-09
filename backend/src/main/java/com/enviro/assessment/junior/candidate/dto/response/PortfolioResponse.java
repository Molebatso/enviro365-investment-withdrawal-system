package com.enviro.assessment.junior.candidate.dto.response;

import com.enviro.assessment.junior.candidate.entity.Portfolio;

import java.math.BigDecimal;
import java.util.List;

/**
 * What the frontend sees for GET /api/investors/{id}/portfolio.
 * The dashboard uses availableBalance directly, and also shows the
 * 90%-of-balance maximum before the investor submits a withdrawal -
 * that maximum is calculated on the frontend from this value, since
 * it is just a display hint (the backend is the source of truth that
 * actually enforces it).
 */
public class PortfolioResponse {

    private final Long id;
    private final BigDecimal availableBalance;
    private final List<InvestmentProductResponse> investmentProducts;

    public PortfolioResponse(Long id, BigDecimal availableBalance, List<InvestmentProductResponse> investmentProducts) {
        this.id = id;
        this.availableBalance = availableBalance;
        this.investmentProducts = investmentProducts;
    }

    public static PortfolioResponse fromEntity(Portfolio portfolio) {
        List<InvestmentProductResponse> products = portfolio.getInvestmentProducts().stream()
                .map(InvestmentProductResponse::fromEntity)
                .toList();

        return new PortfolioResponse(portfolio.getId(), portfolio.getAvailableBalance(), products);
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public List<InvestmentProductResponse> getInvestmentProducts() {
        return investmentProducts;
    }
}
