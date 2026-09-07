package com.enviro.assessment.junior.candidate.repository;

import com.enviro.assessment.junior.candidate.entity.Investor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestorRepository extends JpaRepository<Investor, Long> {
    // findById, findAll, save, etc. are inherited from JpaRepository.
    // No custom queries needed yet.
}
