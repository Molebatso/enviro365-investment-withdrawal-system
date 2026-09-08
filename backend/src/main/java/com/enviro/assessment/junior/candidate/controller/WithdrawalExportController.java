package com.enviro.assessment.junior.candidate.controller;

import com.enviro.assessment.junior.candidate.entity.WithdrawalStatus;
import com.enviro.assessment.junior.candidate.service.CsvExportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * GET /api/withdrawals/export
 * GET /api/withdrawals/export?investorId=1
 * GET /api/withdrawals/export?status=APPROVED
 * GET /api/withdrawals/export?investorId=1&status=APPROVED
 *
 * Both filters are optional and independent. Returns a downloadable
 * CSV file via Content-Disposition, which the browser (or Axios
 * blob response) uses as the suggested filename.
 */
@RestController
@RequestMapping("/api/withdrawals")
public class WithdrawalExportController {

    private final CsvExportService csvExportService;

    public WithdrawalExportController(CsvExportService csvExportService) {
        this.csvExportService = csvExportService;
    }

    @GetMapping("/export")
    public ResponseEntity<String> exportWithdrawals(
            @RequestParam(required = false) Long investorId,
            @RequestParam(required = false) WithdrawalStatus status) {

        String csv = csvExportService.exportToCsv(investorId, status);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/csv"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"withdrawal-statement.csv\"")
                .body(csv);
    }
}
