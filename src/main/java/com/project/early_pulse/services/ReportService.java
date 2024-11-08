package com.project.early_pulse.services;

import com.project.early_pulse.entity.Report;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReportService {
    Mono<Report> uploadReport(String userId, String appointmentId, MultipartFile file);
    Flux<Report> getReportsByUserId(String userId);
    Mono<Report> getReportByAppointmentId(String appointmentId);
}
