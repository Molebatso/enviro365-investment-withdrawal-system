package com.enviro.assessment.junior.candidate.repository;

import com.enviro.assessment.junior.candidate.entity.WithdrawalNotice;
import com.enviro.assessment.junior.candidate.entity.WithdrawalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WithdrawalNoticeRepository extends JpaRepository<WithdrawalNotice, Long> {

    // Powers GET /api/investors/{id}/withdrawals (withdrawal history)
    List<WithdrawalNotice> findByInvestorIdOrderByRequestedDateDesc(Long investorId);

    // Powers GET /api/withdrawals/export?investorId=&status= (CSV export filtering)
    List<WithdrawalNotice> findByInvestorId(Long investorId);

    List<WithdrawalNotice> findByStatus(WithdrawalStatus status);

    List<WithdrawalNotice> findByInvestorIdAndStatus(Long investorId, WithdrawalStatus status);
}
