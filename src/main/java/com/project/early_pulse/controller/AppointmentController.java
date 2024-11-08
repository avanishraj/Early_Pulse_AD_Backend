package com.project.early_pulse.controller;

import com.project.early_pulse.entity.Appointment;
import com.project.early_pulse.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    /**
     * Create a new appointment.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<Appointment> createAppointment(@RequestBody Appointment appointment) {
        return appointmentService.createAppointment(appointment);
    }

    /**
     * Get an appointment by its ID.
     */
    @GetMapping("/{id}")
    public Mono<Appointment> getAppointmentById(@PathVariable String id) {
        return appointmentService.getAppointmentById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Appointment not found with ID: " + id)));
    }

    /**
     * Get all appointments.
     */
    @GetMapping
    public Flux<Appointment> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    @GetMapping("/lab/{labId}")
    public Flux<Appointment> getAppointmentsByLabId(@PathVariable String labId) {
        return appointmentService.getAppointmentsByLabId(labId);
    }

    @GetMapping("/user/{userId}")
    public Flux<Appointment> getAppointmentsByUserId(@PathVariable String userId) {
        return appointmentService.getAppointmentsByUserId(userId);
    }

    /**
     * Update an existing appointment by ID.
     */
    @PutMapping("/{id}")
    public Mono<Appointment> updateAppointment(@PathVariable String id, @RequestBody Appointment appointment) {
        return appointmentService.updateAppointment(id, appointment)
                .switchIfEmpty(Mono.error(new RuntimeException("Appointment not found with ID: " + id)));
    }

    /**
     * Delete an appointment by ID.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteAppointment(@PathVariable String id) {
        return appointmentService.deleteAppointment(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Appointment not found with ID: " + id)));
    }
}
