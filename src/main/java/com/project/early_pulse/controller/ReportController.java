package com.project.early_pulse.controller;

import com.project.early_pulse.entity.Report;
import com.project.early_pulse.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * Admin uploads a report PDF for a specific user and appointment.
     */
    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Report> uploadReport(
            @RequestParam(required = false)  String userId,
            @RequestParam(required = false)  String appointmentId,
            @RequestParam("file") MultipartFile file) {
        return reportService.uploadReport(userId, appointmentId, file);
    }

    /**
     * Retrieve all reports for a specific user by userId.
     */
    @GetMapping("/user/{userId}")
    public Flux<Report> getReportsByUserId(@PathVariable String userId) {
        return reportService.getReportsByUserId(userId);
    }

    /**
     * Retrieve a report for a specific appointment by appointmentId.
     */
    @GetMapping("/appointment/{appointmentId}")
    public Mono<Report> getReportByAppointmentId(@PathVariable String appointmentId) {
        return reportService.getReportByAppointmentId(appointmentId);
    }
}
