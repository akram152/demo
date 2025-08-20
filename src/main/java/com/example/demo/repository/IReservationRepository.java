package com.example.demo.repository;

import com.example.demo.Enum.StatutReservation;
import com.example.demo.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
@Repository
public interface IReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("SELECT p FROM Reservation p WHERE p.expert.id = :expertId AND p.dateRendezvous = :date")
    List<Reservation> findByExpertIdAndDateRendezvous(
            @Param("expertId") int expertId,
            @Param("date") LocalDate date
    );
    boolean existsByExpert_IdAndDateRendezvousAndHeureDebut(int expertId, LocalDate date, LocalTime heureDebut);

    @Query("""
    SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END
    FROM Reservation r
    WHERE r.expert.id = :expertId
      AND r.dateRendezvous = :date AND r.statutReservation <> 'REFUSEE'
      AND (
            (:heureDebut BETWEEN r.heureDebut AND r.heureFin)
         OR (:heureFin BETWEEN r.heureDebut AND r.heureFin)
         OR (r.heureDebut BETWEEN :heureDebut AND :heureFin)
        )
""")
    boolean existsByExpertAndDateRendezvousAndOverlap(
            @Param("expertId") int expertId,
            @Param("date") LocalDate date,
            @Param("heureDebut") LocalTime heureDebut,
            @Param("heureFin") LocalTime heureFin
    );

    List<Reservation> findByExpertId(int expertId);

    List<Reservation> findByClient_Id(int clientId);

    List<Reservation> findByExpert_IdAndStatutReservation(long expertId, StatutReservation statut);

        @Query("SELECT r FROM Reservation r WHERE " +
                "r.dateRendezvous = :date AND r.heureDebut >= :startTime AND r.heureDebut <= :endTime")
        List<Reservation> findByDateAndTimeBetween(@Param("date") LocalDate date,
                                                   @Param("startTime") LocalTime startTime,
                                                   @Param("endTime") LocalTime endTime);
}
