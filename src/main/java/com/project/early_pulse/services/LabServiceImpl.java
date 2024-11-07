package com.project.early_pulse.services;

import com.google.api.core.ApiFuture;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.*;
import com.project.early_pulse.entity.Lab;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class LabServiceImpl implements LabService {

    private static final String COLLECTION_NAME = "labs";
    private final Firestore firestore;

    public LabServiceImpl() {
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
    public Mono<Lab> createLab(Lab lab) {
        CollectionReference labsCollection = firestore.collection(COLLECTION_NAME);
        DocumentReference newDocRef = labsCollection.document();
        lab.setId(newDocRef.getId());

        return Mono.fromFuture(toCompletableFuture(newDocRef.set(lab)))
                .thenReturn(lab)
                .onErrorMap(e -> new RuntimeException("Failed to create lab", e));
    }

    @Override
    public Mono<Lab> getLabById(String id) {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(id);
        return Mono.fromFuture(toCompletableFuture(docRef.get()))
                .mapNotNull(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Lab lab = documentSnapshot.toObject(Lab.class);
                        if (lab != null) {
                            lab.setId(documentSnapshot.getId());
                        }
                        return lab;
                    }
                    return null;
                })
                .onErrorMap(e -> new RuntimeException("Failed to get lab with id: " + id, e));
    }

    @Override
    public Flux<Lab> getAllLabs() {
        CollectionReference labsCollection = firestore.collection(COLLECTION_NAME);
        return Mono.fromFuture(toCompletableFuture(labsCollection.get()))
                .flatMapMany(querySnapshot -> {
                    List<Lab> labs = querySnapshot.getDocuments().stream()
                            .map(doc -> {
                                Lab lab = doc.toObject(Lab.class);
                                lab.setId(doc.getId());
                                return lab;
                            })
                            .collect(Collectors.toList());
                    return Flux.fromIterable(labs);
                })
                .onErrorMap(e -> new RuntimeException("Failed to get all labs", e));
    }

    @Override
    public Mono<Lab> updateLab(String id, Lab lab) {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(id);
        lab.setId(id);
        return Mono.fromFuture(toCompletableFuture(docRef.set(lab)))
                .thenReturn(lab)
                .onErrorMap(e -> new RuntimeException("Failed to update lab with id: " + id, e));
    }

    @Override
    public Mono<Void> deleteLab(String id) {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(id);
        return Mono.fromFuture(toCompletableFuture(docRef.delete()))
                .then()
                .onErrorMap(e -> new RuntimeException("Failed to delete lab with id: " + id, e));
    }
}