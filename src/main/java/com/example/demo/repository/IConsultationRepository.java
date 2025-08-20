package com.example.demo.repository;

import com.example.demo.entities.Consultation;
import com.example.demo.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface IConsultationRepository extends JpaRepository<Consultation, Long> {
 List<Consultation> findConsultationByReservation_Expert_Id(long expertId);
 List<Consultation> findConsultationByReservation_Client_Id(long clientId);
 long countByReservation_Client_Id(long clientId);
 long countByReservation_Expert_Id(long expertId);


}
