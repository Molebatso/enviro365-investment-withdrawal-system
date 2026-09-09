package com.enviro.assessment.junior.candidate.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

/**
 * A single investment product held within a portfolio (e.g. a unit
 * trust, retirement annuity, or money market fund). Purely
 * informational for the dashboard - withdrawal balance checks are
 * performed against Portfolio.availableBalance, not against
 * individual product values.
 */
@Entity
@Table(name = "investment_product")
public class InvestmentProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal currentValue;

    protected InvestmentProduct() {
        // Required by JPA
    }

    public InvestmentProduct(Portfolio portfolio, String productName, BigDecimal currentValue) {
        this.portfolio = portfolio;
        this.productName = productName;
        this.currentValue = currentValue;
    }

    public Long getId() {
        return id;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(BigDecimal currentValue) {
        this.currentValue = currentValue;
    }
}
