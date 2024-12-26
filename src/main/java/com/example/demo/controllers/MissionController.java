package com.example.demo.controllers;

import com.example.demo.entities.Mission;
import com.example.demo.entities.Salaire;
import com.example.demo.services.interfac.IMissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("mission")
public class MissionController {

    @Autowired
    IMissionService iMissionService;

    @GetMapping("getMissionById/{id}")
    public Mission getMissionById(@PathVariable("id") int id) {
        return iMissionService.getMissionById(id);
    }

    @PostMapping
    public Mission createMission(@RequestBody Mission mission) {
        return iMissionService.SaveMission(mission);
    }

    @PutMapping
    public Mission updateMission(@RequestBody Mission mission) {
        return iMissionService.SaveMission(mission);
    }

    @DeleteMapping("delMissionById/{id}")
    public void deleteMissionById(@PathVariable("id") int id) {
        iMissionService.DeleteMissionById(id);
    }

    @PutMapping("assignMissionCollab/{idMission}/{idCollab}")
    public Mission assignSalaireCollab(@PathVariable("idMission") int idMission, @PathVariable("idCollab") int idCollab) {
        return iMissionService.assignMissionCollaborateur(idMission, idCollab);
    }
}
