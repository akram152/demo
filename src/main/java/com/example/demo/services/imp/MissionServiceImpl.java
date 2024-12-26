package com.example.demo.services.imp;

import com.example.demo.entities.Collaborateur;
import com.example.demo.entities.Mission;
import com.example.demo.repository.ICollaborateurRepository;
import com.example.demo.repository.IMissionRepository;
import com.example.demo.services.IMissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MissionServiceImpl implements IMissionService {
    @Autowired
    IMissionRepository imissionRepository;
    @Autowired
    ICollaborateurRepository iCollaborateurRepository;


    @Override
    public Mission getMissionById(int id) {
        return imissionRepository.findById(id).get();
    }

    @Override
    public Mission SaveMission(Mission mission) {
        return imissionRepository.save(mission);
    }

    @Override
    public void DeleteMissionById(int id) {
        imissionRepository.deleteById(id);
    }

    @Override
    public Mission assignMissionCollaborateur(int missionId, int collaborateurId) {
        Mission mission = imissionRepository.findById(missionId).get();
        Collaborateur collab = iCollaborateurRepository.findById(collaborateurId).get();
        mission.setCollaborateur(collab);
        return imissionRepository.save(mission);
    }
}
