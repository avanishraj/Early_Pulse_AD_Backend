package com.project.early_pulse.services;
import com.project.early_pulse.entity.Lab;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LabService {
    Mono<Lab> createLab(Lab lab);
    Mono<Lab> getLabById(String id);
    Flux<Lab> getAllLabs();
    Mono<Lab> updateLab(String id, Lab lab);
    Mono<Void> deleteLab(String id);
}
