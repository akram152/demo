package com.example.demo.controllers;

import com.example.demo.Enum.Mois;
import com.example.demo.entities.Departement;
import com.example.demo.entities.Salaire;
import com.example.demo.services.Interf.ISalaireService;
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

    @GetMapping("getbymoisandannee/{mois}/{annee}")
    public List<Salaire> getByMoisannee(@PathVariable Mois mois, @PathVariable int annee) {
        return salaireService.findSalaireByMoisAndAnnee(mois, annee);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") int id) {
        salaireService.deleteSalaire(id);
        return ResponseEntity.ok("Supprimé");
    }
}
