package com.project.early_pulse.entity;

import com.google.cloud.firestore.annotation.DocumentId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lab {

    @DocumentId
    private String id;
    private String name;
    private String address;
    private String contact;
    private int price;
    private String services;
}
