package com.project.early_pulse.services;

import com.google.api.core.ApiFuture;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.*;
import com.project.early_pulse.entity.Admin;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    private static final String COLLECTION_NAME = "admins";
    private final Firestore firestore;

    public AdminServiceImpl() {
        this.firestore = FirestoreClient.getFirestore();
    }

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
    public Mono<Admin> createAdmin(Admin admin) {
        CollectionReference adminsCollection = firestore.collection(COLLECTION_NAME);
        DocumentReference newDocRef = adminsCollection.document(); // Generate a new document ID
        admin.setId(newDocRef.getId());

        return Mono.fromFuture(toCompletableFuture(newDocRef.set(admin)))
                .thenReturn(admin)
                .onErrorMap(e -> new RuntimeException("Failed to create admin", e));
    }

    @Override
    public Mono<Admin> getAdminById(String id) {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(id);
        return Mono.fromFuture(toCompletableFuture(docRef.get()))
                .mapNotNull(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Admin admin = documentSnapshot.toObject(Admin.class);
                        if (admin != null) {
                            admin.setId(documentSnapshot.getId());
                        }
                        return admin;
                    }
                    return null;
                })
                .onErrorMap(e -> new RuntimeException("Failed to get admin with id: " + id, e));
    }

    @Override
    public Flux<Admin> getAllAdmins() {
        CollectionReference adminsCollection = firestore.collection(COLLECTION_NAME);
        return Mono.fromFuture(toCompletableFuture(adminsCollection.get()))
                .flatMapMany(querySnapshot -> {
                    List<Admin> admins = querySnapshot.getDocuments().stream()
                            .map(doc -> {
                                Admin admin = doc.toObject(Admin.class);
                                admin.setId(doc.getId());
                                return admin;
                            })
                            .collect(Collectors.toList());
                    return Flux.fromIterable(admins);
                })
                .onErrorMap(e -> new RuntimeException("Failed to get all admins", e));
    }

    @Override
    public Mono<Admin> updateAdmin(String id, Admin admin) {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(id);
        admin.setId(id); // Ensure admin object has correct ID for consistency
        return Mono.fromFuture(toCompletableFuture(docRef.set(admin)))
                .thenReturn(admin)
                .onErrorMap(e -> new RuntimeException("Failed to update admin with id: " + id, e));
    }

    @Override
    public Mono<Void> deleteAdmin(String id) {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(id);
        return Mono.fromFuture(toCompletableFuture(docRef.delete()))
                .then()
                .onErrorMap(e -> new RuntimeException("Failed to delete admin with id: " + id, e));
    }

}
