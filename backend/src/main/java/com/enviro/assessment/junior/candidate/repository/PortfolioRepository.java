package com.enviro.assessment.junior.candidate.repository;

import com.enviro.assessment.junior.candidate.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    // Used by the service layer to fetch a portfolio starting from an
    // investor id, since the frontend only ever knows the investor id.
    Optional<Portfolio> findByInvestorId(Long investorId);
}
