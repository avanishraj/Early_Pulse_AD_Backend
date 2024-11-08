package com.project.early_pulse.controller;

import com.project.early_pulse.entity.Admin;
import com.project.early_pulse.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * Create a new admin.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<Admin> createAdmin(@RequestBody Admin admin) {
        return adminService.createAdmin(admin);
    }

    /**
     * Get an admin by its ID.
     */
    @GetMapping("/{id}")
    public Mono<Admin> getAdminById(@PathVariable String id) {
        return adminService.getAdminById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Admin not found with ID: " + id)));
    }

    /**
     * Get all admins.
     */
    @GetMapping
    public Flux<Admin> getAllAdmins() {
        return adminService.getAllAdmins();
    }

    /**
     * Update an existing admin by ID.
     */
    @PutMapping("/{id}")
    public Mono<Admin> updateAdmin(@PathVariable String id, @RequestBody Admin admin) {
        return adminService.updateAdmin(id, admin)
                .switchIfEmpty(Mono.error(new RuntimeException("Admin not found with ID: " + id)));
    }

    /**
     * Delete an admin by ID.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteAdmin(@PathVariable String id) {
        return adminService.deleteAdmin(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Admin not found with ID: " + id)));
    }
}
