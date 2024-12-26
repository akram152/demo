package com.example.demo.services;

import com.example.demo.entities.Mission;

public interface IMissionService {
    Mission getMissionById(int id);
    Mission SaveMission(Mission mission);
    void DeleteMissionById(int id);
    Mission assignMissionCollaborateur(int missionId, int collaborateurId);




}
