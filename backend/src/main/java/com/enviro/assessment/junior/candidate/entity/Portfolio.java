package com.enviro.assessment.junior.candidate.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * A single investor's portfolio: an available balance plus the
 * investment products that make it up.
 *
 * availableBalance is the figure the withdrawal business rules are
 * evaluated against. It is decremented by WithdrawalService whenever
 * a withdrawal is approved (see WithdrawalService for the update
 * logic and rationale).
 */
@Entity
@Table(name = "portfolio")
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "investor_id", nullable = false, unique = true)
    private Investor investor;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal availableBalance;

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvestmentProduct> investmentProducts = new ArrayList<>();

    protected Portfolio() {
        // Required by JPA
    }

    public Portfolio(Investor investor, BigDecimal availableBalance) {
        this.investor = investor;
        this.availableBalance = availableBalance;
    }

    public Long getId() {
        return id;
    }

    public Investor getInvestor() {
        return investor;
    }

    public void setInvestor(Investor investor) {
        this.investor = investor;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    public List<InvestmentProduct> getInvestmentProducts() {
        return investmentProducts;
    }
}
