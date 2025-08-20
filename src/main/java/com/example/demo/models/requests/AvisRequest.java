package com.example.demo.models.requests;

import lombok.Data;

@Data
public class AvisRequest {
    private long consultationId;
    private int note;
    private String commentaire;
}
