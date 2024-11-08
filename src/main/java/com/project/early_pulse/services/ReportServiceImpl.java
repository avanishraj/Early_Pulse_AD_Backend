package com.project.early_pulse.services;

import com.google.api.core.ApiFuture;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.StorageClient;
import com.project.early_pulse.entity.Report;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    private static final String COLLECTION_NAME = "reports";
    private final Firestore firestore;

    public ReportServiceImpl() {
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
    public Mono<Report> uploadReport(String userId, String appointmentId, MultipartFile file) {
        String reportId = UUID.randomUUID().toString();
        String storagePath = "reports/" + reportId + ".pdf";

        try {
            InputStream fileInputStream = file.getInputStream();
            StorageClient.getInstance().bucket().create(storagePath, fileInputStream, file.getContentType());

            String reportUrl = StorageClient.getInstance().bucket().get(storagePath).getMediaLink();
            Report report = new Report(reportId, userId, appointmentId, reportUrl, LocalDateTime.now());

            DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(reportId);
            return Mono.fromFuture(toCompletableFuture(docRef.set(report)))
                    .then(Mono.just(report))
                    .doOnSuccess(r -> sendReportNotificationToUser(userId, "Your report is available", reportUrl))
                    .onErrorMap(e -> new RuntimeException("Failed to upload report", e));
        } catch (Exception e) {
            return Mono.error(new RuntimeException("Failed to upload report", e));
        }
    }

    @Override
    public Flux<Report> getReportsByUserId(String userId) {
        CollectionReference reportsCollection = firestore.collection(COLLECTION_NAME);
        return Mono.fromFuture(toCompletableFuture(reportsCollection.whereEqualTo("userId", userId).get()))
                .flatMapMany(querySnapshot -> {
                    List<Report> reports = querySnapshot.getDocuments().stream()
                            .map(doc -> doc.toObject(Report.class))
                            .collect(Collectors.toList());
                    return Flux.fromIterable(reports);
                });
    }

    @Override
    public Mono<Report> getReportByAppointmentId(String appointmentId) {
        CollectionReference reportsCollection = firestore.collection(COLLECTION_NAME);
        return Mono.fromFuture(toCompletableFuture(reportsCollection.whereEqualTo("appointmentId", appointmentId).get()))
                .flatMap(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        return Mono.just(querySnapshot.getDocuments().get(0).toObject(Report.class));
                    }
                    return Mono.empty();
                });
    }

    private void sendReportNotificationToUser(String userId, String message, String reportUrl) {
        // Send notification using Firebase Cloud Messaging or another notification service
        System.out.println("Notification sent to user " + userId + ": " + message + ". View at: " + reportUrl);
    }
}
