package com.example.demo.utils.mappers;

import com.example.demo.entities.Utilisateur;
import com.example.demo.models.requests.ExpertRequest;
import com.example.demo.models.responses.ExpertResponse;

public class ExpertMappers {
    public Utilisateur requestToEntity(ExpertRequest expertRequest) {
        Utilisateur expertEntity = new Utilisateur();
        expertEntity.setId(expertRequest.getId());
        expertEntity.setNom(expertRequest.getNom());
        expertEntity.setPrenom(expertRequest.getPrenom());
        expertEntity.setBio(expertRequest.getBio());
        expertEntity.setImageProfil(expertRequest.getImageProfil().getOriginalFilename());
        expertEntity.setAnneesExperience(expertRequest.getAnneesExperience());
        expertEntity.setEmail(expertRequest.getEmail());
        expertEntity.setTelephone(expertRequest.getTelephone());
        expertEntity.setDateNaissance(expertRequest.getDateNaissance());
        expertEntity.setCertification(expertRequest.getCertification());
        expertEntity.setCvPath(expertRequest.getCvFile().getOriginalFilename());
        expertEntity.setDomaineExpertise(expertRequest.getDomaineExpertise());
        return expertEntity;
    }

        public ExpertResponse entityToResponse(Utilisateur expertEntity) {
            ExpertResponse expertResponse = new ExpertResponse();
            expertResponse.setNom(expertEntity.getNom());
            expertResponse.setPrenom(expertEntity.getPrenom());
            expertResponse.setBio(expertEntity.getBio());
            expertResponse.setImageProfil(expertEntity.getImageProfil());
            expertResponse.setAnneesExperience(expertEntity.getAnneesExperience());
            expertResponse.setEmail(expertEntity.getEmail());
            expertResponse.setTelephone(expertEntity.getTelephone());
            expertResponse.setCertification(expertEntity.getCertification());
            expertResponse.setCvFile(expertEntity.getCvPath());
            expertResponse.setDomaineExpertise(expertEntity.getDomaineExpertise());
            expertResponse.setId(expertEntity.getId());
            expertResponse.setDateNaissance(expertEntity.getDateNaissance());
            return expertResponse;
        }
}
