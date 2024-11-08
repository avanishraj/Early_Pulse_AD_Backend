package com.project.early_pulse.entity;

import com.google.cloud.firestore.annotation.DocumentId;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @DocumentId
    private String id;
    private String userId;
    private LocalDateTime appointmentDate;
    private String status;
    private String labId;
}
