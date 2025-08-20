package com.example.demo.services;

import com.example.demo.models.requests.AvisRequest;
import com.example.demo.models.responses.AvisResponse;

import java.util.List;

public interface IAvisService {
    AvisResponse ajouterAvis(AvisRequest request);

    AvisResponse getAvisByConsultation(long consultationId);

    List<AvisResponse> getAvisForExpert();

    boolean avisDejaDonnePourConsultation(long consultationId);
}
