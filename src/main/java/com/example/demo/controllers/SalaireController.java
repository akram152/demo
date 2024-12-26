package com.example.demo.controllers;

import com.example.demo.entities.Salaire;
import com.example.demo.services.interfac.ISalaireService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("salaire")
public class SalaireController {

    private final ISalaireService salaireService;

    private SalaireController(ISalaireService salaireService) {
        this.salaireService = salaireService;
    }

    @PostMapping
    public Salaire saveSalaire(@RequestBody Salaire salaire) {
        return salaireService.saveOrUpdateSalaire(salaire);
    }

    @PutMapping
    public Salaire updateSalaire(@RequestBody Salaire salaire) throws Exception {
        return salaireService.saveOrUpdateSalaire(salaire);
    }

    @GetMapping("/id/{id}")
    public Salaire getSalaireById(@PathVariable int id) {
        return salaireService.getSalaireById(id);
    }

    @GetMapping
    public ResponseEntity<List<Salaire>> getAllSalaires() {
        List<Salaire> salaires = salaireService.getAllSalaires();
        if (salaires.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(salaires);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<String> deleteById(@PathVariable int id) {
        salaireService.deleteSalaire(id);
        return ResponseEntity.ok("Supprimé");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAll() {
        salaireService.deleteAllSalaire();
        return ResponseEntity.ok("Supprimé toutes les salaires");
    }

    @PutMapping("assignSalaireCollab/{idSalaire}/{idCollab}")
    public Salaire assignSalaireCollab(@PathVariable("idSalaire") int idSalaire, @PathVariable("idCollab") int idCollab) {
        return salaireService.assignSalaireCollaborateur(idSalaire, idCollab);
    }


}
