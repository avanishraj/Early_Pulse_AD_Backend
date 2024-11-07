package com.project.early_pulse.services;

import com.google.api.core.ApiFuture;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.*;
import com.project.early_pulse.entity.Appointment;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private static final String COLLECTION_NAME = "appointments";
    private final Firestore firestore;

    public AppointmentServiceImpl() {
        this.firestore = FirestoreClient.getFirestore();
    }

    // Utility method to convert ApiFuture to CompletableFuture
    private <T> CompletableFuture<T> toCompletableFuture(ApiFuture<T> apiFuture) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        apiFuture.addListener(() -> {
            try {
                completableFuture.complete(apiFuture.get());
            } catch (Exception e) {
                completableFuture.completeExceptionally(e);
            }
        }, Runnable::run);
        return completableFuture;
    }

    @Override
    public Mono<Appointment> createAppointment(Appointment appointment) {
        CollectionReference appointmentsCollection = firestore.collection(COLLECTION_NAME);
        DocumentReference newDocRef = appointmentsCollection.document(); // Generate a new document ID
        appointment.setId(newDocRef.getId());

        return Mono.fromFuture(toCompletableFuture(newDocRef.set(appointment)))
                .thenReturn(appointment)
                .onErrorMap(e -> new RuntimeException("Failed to create appointment", e));
    }

    @Override
    public Mono<Appointment> getAppointmentById(String id) {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(id);
        return Mono.fromFuture(toCompletableFuture(docRef.get()))
                .mapNotNull(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Appointment appointment = documentSnapshot.toObject(Appointment.class);
                        if (appointment != null) {
                            appointment.setId(documentSnapshot.getId());
                        }
                        return appointment;
                    }
                    return null;
                })
                .onErrorMap(e -> new RuntimeException("Failed to get appointment with id: " + id, e));
    }

    @Override
    public Flux<Appointment> getAllAppointments() {
        CollectionReference appointmentsCollection = firestore.collection(COLLECTION_NAME);
        return Mono.fromFuture(toCompletableFuture(appointmentsCollection.get()))
                .flatMapMany(querySnapshot -> {
                    List<Appointment> appointments = querySnapshot.getDocuments().stream()
                            .map(doc -> {
                                Appointment appointment = doc.toObject(Appointment.class);
                                appointment.setId(doc.getId());
                                return appointment;
                            })
                            .collect(Collectors.toList());
                    return Flux.fromIterable(appointments);
                })
                .onErrorMap(e -> new RuntimeException("Failed to get all appointments", e));
    }

    @Override
    public Flux<Appointment> getAppointmentsByLabId(String labId) {
        CollectionReference appointmentsCollection = firestore.collection(COLLECTION_NAME);
        return Mono.fromFuture(toCompletableFuture(appointmentsCollection.whereEqualTo("labId", labId).get()))
                .flatMapMany(querySnapshot -> {
                    List<Appointment> appointments = querySnapshot.getDocuments().stream()
                            .map(doc -> {
                                Appointment appointment = doc.toObject(Appointment.class);
                                appointment.setId(doc.getId());
                                return appointment;
                            })
                            .collect(Collectors.toList());
                    return Flux.fromIterable(appointments);
                })
                .onErrorMap(e -> new RuntimeException("Failed to get appointments for labId: " + labId, e));
    }

    @Override
    public Flux<Appointment> getAppointmentsByUserId(String userId) {
        CollectionReference appointmentsCollection = firestore.collection(COLLECTION_NAME);
        return Mono.fromFuture(toCompletableFuture(appointmentsCollection.whereEqualTo("userId", userId).get()))
                .flatMapMany(querySnapshot -> {
                    List<Appointment> appointments = querySnapshot.getDocuments().stream()
                            .map(doc -> {
                                Appointment appointment = doc.toObject(Appointment.class);
                                appointment.setId(doc.getId());
                                return appointment;
                            })
                            .collect(Collectors.toList());
                    return Flux.fromIterable(appointments);
                })
                .onErrorMap(e -> new RuntimeException("Failed to get appointments for userId: " + userId, e));
    }

    @Override
    public Mono<Appointment> updateAppointment(String id, Appointment appointment) {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(id);
        appointment.setId(id); // Ensure appointment object has correct ID
        return Mono.fromFuture(toCompletableFuture(docRef.set(appointment)))
                .thenReturn(appointment)
                .onErrorMap(e -> new RuntimeException("Failed to update appointment with id: " + id, e));
    }

    @Override
    public Mono<Void> deleteAppointment(String id) {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(id);
        return Mono.fromFuture(toCompletableFuture(docRef.delete()))
                .then()
                .onErrorMap(e -> new RuntimeException("Failed to delete appointment with id: " + id, e));
    }
}
