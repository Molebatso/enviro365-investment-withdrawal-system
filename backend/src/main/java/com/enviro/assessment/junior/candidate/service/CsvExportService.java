package com.enviro.assessment.junior.candidate.service;

import com.enviro.assessment.junior.candidate.entity.WithdrawalNotice;
import com.enviro.assessment.junior.candidate.entity.WithdrawalStatus;
import com.enviro.assessment.junior.candidate.repository.WithdrawalNoticeRepository;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Builds a CSV withdrawal statement, optionally filtered by investor
 * and/or status. Kept separate from WithdrawalService since this is a
 * reporting/formatting concern, not a business rule concern.
 */
@Service
public class CsvExportService {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String[] HEADER = {
            "Withdrawal ID", "Investor ID", "Investor Name", "Withdrawal Type", "Amount", "Date", "Status"
    };

    private final WithdrawalNoticeRepository withdrawalNoticeRepository;

    public CsvExportService(WithdrawalNoticeRepository withdrawalNoticeRepository) {
        this.withdrawalNoticeRepository = withdrawalNoticeRepository;
    }

    /**
     * investorId and/or status may be null, meaning "no filter on that
     * field". Do not over-engineer: four simple cases cover every
     * supported combination.
     */
    public String exportToCsv(Long investorId, WithdrawalStatus status) {
        List<WithdrawalNotice> notices;

        if (investorId != null && status != null) {
            notices = withdrawalNoticeRepository.findByInvestorIdAndStatus(investorId, status);
        } else if (investorId != null) {
            notices = withdrawalNoticeRepository.findByInvestorId(investorId);
        } else if (status != null) {
            notices = withdrawalNoticeRepository.findByStatus(status);
        } else {
            notices = withdrawalNoticeRepository.findAll();
        }

        StringBuilder csv = new StringBuilder();
        csv.append(String.join(",", HEADER)).append("\n");

        for (WithdrawalNotice notice : notices) {
            csv.append(toCsvRow(notice)).append("\n");
        }

        return csv.toString();
    }

    private String toCsvRow(WithdrawalNotice notice) {
        return String.join(",",
                String.valueOf(notice.getId()),
                String.valueOf(notice.getInvestor().getId()),
                escapeCsvField(notice.getInvestor().getFullName()),
                notice.getWithdrawalType().name(),
                notice.getAmount().toPlainString(),
                notice.getRequestedDate().format(DATE_FORMAT),
                notice.getStatus().name()
        );
    }

    /**
     * Wraps a field in double quotes (and escapes existing quotes) if
     * it contains a comma, quote, or newline - standard CSV escaping.
     * Investor names in the seed data don't need it, but real names
     * could contain commas (e.g. "Nkosi, Jr").
     */
    private String escapeCsvField(String field) {
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }
}
