package com.enviro.assessment.junior.candidate.repository;

import com.enviro.assessment.junior.candidate.entity.InvestmentProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestmentProductRepository extends JpaRepository<InvestmentProduct, Long> {
    // Investment products are always accessed through their parent
    // Portfolio (portfolio.getInvestmentProducts()), so no custom
    // queries are needed here yet.
}
