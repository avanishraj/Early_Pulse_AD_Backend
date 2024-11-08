package com.project.early_pulse.controller;
import com.project.early_pulse.entity.Lab;
import com.project.early_pulse.services.LabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/labs")
public class LabController {

    private final LabService labService;
    @GetMapping("/test")
    public String test(String testMessage){
        return "Hi, Welcome to Early Pulse platform";
    }

    @Autowired
    public LabController(LabService labService) {
        this.labService = labService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<Lab> createLab(@RequestBody Lab lab) {
        return labService.createLab(lab);
    }

    @GetMapping("/{id}")
    public Mono<Lab> getLabById(@PathVariable String id) {
        return labService.getLabById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Lab not found with ID: " + id)));
    }

    /**
     * Get all labs.
     */
    @GetMapping
    public Flux<Lab> getAllLabs() {
        return labService.getAllLabs();
    }

    @PutMapping("/{id}")
    public Mono<Lab> updateLab(@PathVariable String id, @RequestBody Lab lab) {
        return labService.updateLab(id, lab)
                .switchIfEmpty(Mono.error(new RuntimeException("Lab not found with ID: " + id)));
    }

    /**
     * Delete a lab by ID.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteLab(@PathVariable String id) {
        return labService.deleteLab(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Lab not found with ID: " + id)));
    }
}
