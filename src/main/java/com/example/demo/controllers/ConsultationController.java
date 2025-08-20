        package com.example.demo.controllers;

        import com.example.demo.entities.Consultation;
        import com.example.demo.models.requests.ConsultationRequest;
        import com.example.demo.models.responses.ConsultationResponse;
        import com.example.demo.services.IConsultationService;
        import lombok.RequiredArgsConstructor;
        import org.springframework.core.io.Resource;
        import org.springframework.http.ContentDisposition;
        import org.springframework.http.HttpHeaders;
        import org.springframework.http.ResponseEntity;
        import org.springframework.web.bind.annotation.*;
        import org.springframework.web.multipart.MultipartFile;

        import java.io.IOException;
        import java.util.List;

        @RestController
        @RequestMapping("consultation")
        @RequiredArgsConstructor
        @CrossOrigin(origins = "http://localhost:4200")
        public class ConsultationController {
            private final IConsultationService consultationService;

                @PostMapping("/start/{reservationId}")
                public ResponseEntity<ConsultationResponse> startConsultation(@PathVariable long reservationId) {
                    return ResponseEntity.ok(consultationService.startConsultation(reservationId));
                }

            @PutMapping("/end/{consultationId}")
            public ResponseEntity<String> endConsultation(
                    @PathVariable long consultationId) {

                consultationService.endConsultation(consultationId);
                return ResponseEntity.ok("Consultation terminée avec succès.");
            }


            @GetMapping("/historique")
            public ResponseEntity<?> getHistoriqueConsultation() {
                return ResponseEntity.ok(consultationService.getConsultationsHistorique());
            }

            @PostMapping("/{id}/fichiers")
            public ResponseEntity<String> uploadFile(@PathVariable long id, @RequestParam("file") MultipartFile file) {
                String relativePath = consultationService.uploadFile(id, file);
                return ResponseEntity.ok(relativePath);
            }

            @GetMapping("/{id}/fichiers")
            public ResponseEntity<List<String>> listFiles(@PathVariable long id) throws IOException {
                return ResponseEntity.ok(consultationService.listFiles(id));
            }

            @GetMapping("/{id}/fichiers/download/{fileName:.+}")
            public ResponseEntity<Resource> download(@PathVariable long id, @PathVariable String fileName) throws IOException {
                Resource resource = consultationService.downloadFile(id, fileName);

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                ContentDisposition.attachment().filename(fileName).build().toString())
                        .body(resource);
            }

            @PutMapping("/{id}/resume")
            public ResponseEntity<?> ajouterResume(@PathVariable long id,
                                                   @RequestBody ConsultationRequest request) {
                consultationService.ajouterResume(id, request.getResume());
                return ResponseEntity.ok("Résumé ajouté avec succès.");
            }

            @GetMapping("/{id}")
            public ResponseEntity<ConsultationResponse> getConsultationById(@PathVariable long id) {
                ConsultationResponse response = consultationService.findConsultationById(id);
                return ResponseEntity.ok(response);
            }

            @GetMapping("/nombreConsultation")
            public ResponseEntity<Long> getNombreConsultations() {
                return ResponseEntity.ok(consultationService.getNombreConsultationsPourUtilisateur());
            }

        }
