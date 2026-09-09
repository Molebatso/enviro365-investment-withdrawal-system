package com.enviro.assessment.junior.candidate.dto.response;

import com.enviro.assessment.junior.candidate.entity.InvestmentProduct;

import java.math.BigDecimal;

/**
 * What the frontend sees for a single investment product.
 * Deliberately excludes the internal portfolio_id foreign key -
 * the frontend never needs it, and exposing it would leak
 * database structure across the API boundary.
 */
public class InvestmentProductResponse {

    private final Long id;
    private final String productName;
    private final BigDecimal currentValue;

    public InvestmentProductResponse(Long id, String productName, BigDecimal currentValue) {
        this.id = id;
        this.productName = productName;
        this.currentValue = currentValue;
    }

    public static InvestmentProductResponse fromEntity(InvestmentProduct product) {
        return new InvestmentProductResponse(
                product.getId(),
                product.getProductName(),
                product.getCurrentValue()
        );
    }

    public Long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getCurrentValue() {
        return currentValue;
    }
}
