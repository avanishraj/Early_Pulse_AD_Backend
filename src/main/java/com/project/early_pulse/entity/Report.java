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

    @DocumentId
    private String id;
    private String userId;
    private String reportUrl;
    private LocalDateTime uploadedDate;
    private String appointmentId; // Store Appointment ID as a string reference
}
