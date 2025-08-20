package com.example.demo.controllers;

import com.example.demo.models.requests.AvisRequest;
import com.example.demo.models.responses.AvisResponse;
import com.example.demo.services.IAvisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/avis")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AvisController {
    private final IAvisService avisService;

    @PostMapping
    public ResponseEntity<AvisResponse> ajouterAvis(@RequestBody AvisRequest     request) {
        AvisResponse response = avisService.ajouterAvis(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAvis/{consultationId}")
    public ResponseEntity<AvisResponse> getAvisByConsultation(@PathVariable long consultationId) {
        return ResponseEntity.ok(avisService.getAvisByConsultation(consultationId));
    }

    @GetMapping("/mesAvis")
    public ResponseEntity<List<AvisResponse>> getAvisPourExpert() {
        List<AvisResponse> avisResponses = avisService.getAvisForExpert();
        return ResponseEntity.ok(avisResponses);
    }

    @GetMapping("/existe/{consultationId}")
    public ResponseEntity<Boolean> avisExistePourConsultation(@PathVariable long consultationId) {
        boolean existe = avisService.avisDejaDonnePourConsultation(consultationId);
        return ResponseEntity.ok(existe);
    }

}
