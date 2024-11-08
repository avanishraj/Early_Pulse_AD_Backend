package com.project.early_pulse.entity;

import com.google.cloud.firestore.annotation.DocumentId;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    private String id;
    private String userId;
    private String appointmentId;
    private String reportUrl;
    private LocalDateTime uploadedDate;
}
