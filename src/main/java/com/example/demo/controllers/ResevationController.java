package com.example.demo.controllers;


import com.example.demo.models.requests.ReservationRequest;
import com.example.demo.models.responses.ReservationResponse;
import com.example.demo.services.IReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("reservation")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ResevationController {
    private final IReservationService reservationService;


    @GetMapping("/getCreneauxDisponibles")
    public List<LocalDateTime> getCreneauxDisponibles( @RequestParam int expertId,@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return reservationService.getCreneauxDisponibles(expertId,date);
    }

    @PostMapping("/reserver")
    public ResponseEntity<?> reserver(@RequestBody ReservationRequest request) {
        reservationService.reserver(request);
        return ResponseEntity.ok().build(); // ✅ OK
    }


        @GetMapping("/expert/reservations")
        public List<ReservationResponse> getReservationsExpert(
                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
            return reservationService.getReservationsExpert(date);
        }

    @PutMapping("/accepterRefuserReservation/{reservationId}")
    public void  traiterReservation(@PathVariable long reservationId, @RequestParam Boolean accepte ) {
        reservationService.traiterReservation(reservationId,accepte);
    }

    @DeleteMapping("/annulerReserv/{id}")
    public void annulerReserv(@PathVariable long id) {
        reservationService.annulerReservation(id);
    }

    @GetMapping("/client/mes-reservations")
    public List<ReservationResponse> getReservationsClient(
            @RequestParam(required = false) String statut,  // "EN_ATTENTE", "ACCEPTEE", "REFUSEE"
            @RequestParam(required = false) Boolean passes ,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date   // true => rendez-vous passés
    ) {
        return reservationService.getReservationsClient(statut, passes,date);
    }

    @GetMapping("/expert/agenda")
    public List<ReservationResponse> getAgendaExpert() {
        return reservationService.getAgendaExpert();
    }






}
