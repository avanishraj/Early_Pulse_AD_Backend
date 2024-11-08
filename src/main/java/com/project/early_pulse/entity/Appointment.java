package com.project.early_pulse.entity;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @DocumentId
    private String id;
    private String userId;
    private Timestamp appointmentDate;
    private String status;
    private String labId;

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        if (appointmentDate != null) {
            this.appointmentDate = Timestamp.ofTimeSecondsAndNanos(
                    appointmentDate.toEpochSecond(ZoneOffset.UTC),
                    appointmentDate.getNano());
        }
    }

    public LocalDateTime getAppointmentDate() {
        if (this.appointmentDate != null) {
            return LocalDateTime.ofInstant(this.appointmentDate.toDate().toInstant(), ZoneId.of("UTC"));
        }
        return null;
    }

}
