package com.project.early_pulse.services;

import com.project.early_pulse.entity.Appointment;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AppointmentService {
    Mono<Appointment> createAppointment(Appointment appointment);
    Mono<Appointment> getAppointmentById(String id);
    Flux<Appointment> getAllAppointments();
    Flux<Appointment> getAppointmentsByLabId(String labId);
    Flux<Appointment> getAppointmentsByUserId(String userId);
    Mono<Appointment> updateAppointment(String id, Appointment appointment);
    Mono<Void> deleteAppointment(String id);
}
