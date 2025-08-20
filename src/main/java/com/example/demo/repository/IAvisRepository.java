package com.example.demo.repository;

import com.example.demo.entities.Avis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAvisRepository extends JpaRepository<Avis, Long> {
    Optional<Avis> findByConsultation_Id(long consultationId);

    List<Avis> findAvisByExpert_Id(long expertId);

    boolean existsByConsultationId(long consultationId);

    boolean existsByConsultation_IdAndClient_Id(long consultationId, long clientId);


}
