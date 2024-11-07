package com.project.early_pulse.services;

import com.project.early_pulse.entity.Admin;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

public interface AdminService {
    Mono<Admin> createAdmin(Admin admin);
    Mono<Admin> getAdminById(String id);
    Flux<Admin> getAllAdmins();
    Mono<Admin> updateAdmin(String id, Admin admin);
    Mono<Void> deleteAdmin(String id);
}
