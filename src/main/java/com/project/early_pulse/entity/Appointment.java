package com.project.early_pulse.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.google.cloud.firestore.annotation.DocumentId;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @DocumentId
    private String id;
    private String userId;
    private String userName;
    private String appointmentDate;
    private String status;
    private String labId;
}
