package com.project.early_pulse.services;

import com.project.early_pulse.entity.Appointment;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AppointmentService {
    Mono<Appointment> createAppointment(Appointment appointment);
    Mono<Appointment> getAppointmentById(String id);
    Flux<Appointment> getAllAppointments();
    Flux<Appointment> getAppointmentsByLabId(String labId); // To fetch appointments for a specific lab
    Flux<Appointment> getAppointmentsByUserId(String userId); // To fetch appointments for a specific user
    Mono<Appointment> updateAppointment(String id, Appointment appointment);
    Mono<Void> deleteAppointment(String id);
}
